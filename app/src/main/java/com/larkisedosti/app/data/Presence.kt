package com.larkisedosti.app.data

import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PresenceState(
    val online: Boolean,
    val lastChanged: Long,
)

class Presence(
    private val currentUid: () -> String?,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var wantOnline = false
    private var connectionListener: ValueEventListener? = null
    private var lastMirrorAt = 0L
    private var mirrorGeneration = 0
    private var mirrorJob: Job? = null
    private val scopes = mutableMapOf<String, Set<String>>()
    private val listeners = mutableMapOf<String, ValueEventListener>()

    var onUpdate: ((String, Boolean, Long) -> Unit)? = null
    var onClear: ((String) -> Unit)? = null

    fun setWanted(online: Boolean) {
        val uid = currentUid() ?: return
        wantOnline = online
        val database = databaseOrNull() ?: run {
            mirror(uid, online)
            return
        }
        ensureConnectionWatch(database)
        val ref = database.reference.child("status").child(uid)
        if (online) {
            armDisconnect(ref, uid)
        } else {
            ref.setValue(payload("offline"))
        }
        mirror(uid, online)
    }

    fun clear() {
        val uid = currentUid() ?: return
        wantOnline = false
        mirrorJob?.cancel()
        val ref = databaseOrNull()?.reference?.child("status")?.child(uid)
        ref?.onDisconnect()?.cancel()
        ref?.setValue(payload("offline"))
        writeMirror(uid, false)
    }

    fun watch(scope: String, ids: Collection<String>) {
        scopes[scope] = ids.filter { it.isNotBlank() }.toSet()
        val database = databaseOrNull() ?: return
        val wanted = scopes.values.flatten().toSet()
        (listeners.keys - wanted).forEach { id ->
            val listener = listeners.remove(id) ?: return@forEach
            database.reference.child("status").child(id).removeEventListener(listener)
        }
        (wanted - listeners.keys).forEach { id ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        onClear?.invoke(id)
                        return
                    }
                    val state = snapshot.child("state").getValue(String::class.java)
                    val changed = epochMillis(snapshot.child("lastChanged").value)
                    onUpdate?.invoke(id, state == "online", changed)
                }

                override fun onCancelled(error: DatabaseError) = Unit
            }
            database.reference.child("status").child(id).addValueEventListener(listener)
            listeners[id] = listener
        }
    }

    private fun ensureConnectionWatch(database: FirebaseDatabase) {
        if (connectionListener != null) return
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) == true
                val uid = currentUid()
                if (!connected || !wantOnline || uid == null) return
                armDisconnect(database.reference.child("status").child(uid), uid)
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }
        database.reference.child(".info/connected").addValueEventListener(listener)
        connectionListener = listener
    }

    private fun armDisconnect(ref: DatabaseReference, uid: String) {
        ref.onDisconnect().setValue(payload("offline")).addOnSuccessListener {
            if (wantOnline && currentUid() == uid) {
                ref.setValue(payload("online"))
            }
        }
    }

    private fun mirror(uid: String, online: Boolean) {
        val now = System.currentTimeMillis()
        val wait = 60_000L - (now - lastMirrorAt)
        mirrorJob?.cancel()
        if (wait <= 0L) {
            writeMirror(uid, online)
            return
        }
        mirrorJob = scope.launch {
            delay(wait)
            val current = currentUid()
            if (current == uid) writeMirror(uid, wantOnline)
        }
    }

    private fun writeMirror(uid: String, online: Boolean) {
        val now = System.currentTimeMillis()
        val generation = ++mirrorGeneration
        lastMirrorAt = now
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .update(mapOf("isOnline" to online, "lastSeen" to now))
            .addOnFailureListener {
                if (generation == mirrorGeneration) lastMirrorAt = 0L
            }
    }

    private fun payload(state: String): Map<String, Any> {
        return mapOf(
            "state" to state,
            "lastChanged" to ServerValue.TIMESTAMP,
        )
    }

    private fun databaseOrNull(): FirebaseDatabase? {
        return try {
            val app = FirebaseApp.getInstance()
            val url = app.options.databaseUrl
            if (url.isNullOrBlank()) null else FirebaseDatabase.getInstance(app)
        } catch (_: Exception) {
            null
        }
    }

    private fun epochMillis(value: Any?): Long {
        return when (value) {
            is Long -> value
            is Int -> value.toLong()
            is Double -> value.toLong()
            else -> 0L
        }
    }
}
