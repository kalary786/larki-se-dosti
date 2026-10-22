package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.data.LinkGoogleResult
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.Countries
import com.larkisedosti.app.model.Country
import com.larkisedosti.app.model.Gender
import com.larkisedosti.app.ui.components.BottomSheet
import com.larkisedosti.app.ui.components.GhostButton
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.LoungeTextField
import com.larkisedosti.app.ui.components.ModalDialog
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.components.SecondaryButton
import com.larkisedosti.app.ui.components.SettingsRow
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import android.app.Activity
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    store: AppStore,
    onBack: () -> Unit,
) {
    val colors = LoungeTheme.colors
    var name by remember { mutableStateOf(store.name) }
    var gender by remember { mutableStateOf(store.gender) }
    var country by remember { mutableStateOf(store.country) }
    var open by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(LoungeSpace.s20),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircleButton(AppIcons.ChevronLeft, onBack, contentDescription = "Back", size = 40.dp)
            Spacer(Modifier.size(LoungeSpace.s12))
            Text(text = "Edit profile", color = colors.textPrimary, style = LoungeType.h2)
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        LoungeTextField(value = name, onValueChange = { name = it }, placeholder = "Name")
        Spacer(Modifier.height(LoungeSpace.s16))
        Text(text = "Date of birth", color = colors.textSecondary, style = LoungeType.caption)
        Spacer(Modifier.height(6.dp))
        Text(
            text = formatDateOfBirth(store.dateOfBirth),
            color = colors.textPrimary,
            style = LoungeType.body,
            modifier = Modifier
                .fillMaxWidth()
                .clipSafe()
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.input)
                .padding(LoungeSpace.s16),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Date of birth can't be changed after the account is created.",
            color = colors.textSecondary,
            style = LoungeType.caption,
        )
        if (store.username.isNotBlank()) {
            Spacer(Modifier.height(LoungeSpace.s16))
            Text(text = "Username", color = colors.textSecondary, style = LoungeType.caption)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "@${store.username}",
                color = colors.textPrimary,
                style = LoungeType.body,
                modifier = Modifier
                    .fillMaxWidth()
                    .clipSafe()
                    .background(colors.surface)
                    .border(1.dp, colors.border, LoungeShape.input)
                    .padding(LoungeSpace.s16),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Username can't be changed.",
                color = colors.textSecondary,
                style = LoungeType.caption,
            )
        }
        Spacer(Modifier.height(LoungeSpace.s16))
        Gender.entries.forEach { option ->
            val active = option == gender
            Text(
                text = option.label,
                color = if (active) colors.onPrimary else colors.textPrimary,
                style = LoungeType.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LoungeSpace.s8)
                    .clipSafe()
                    .background(if (active) colors.primary else colors.surface)
                    .border(1.dp, if (active) colors.primary else colors.border, LoungeShape.card)
                    .clickable { gender = option }
                    .padding(LoungeSpace.s16),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clipSafe()
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.input)
                .clickable { open = true }
                .padding(LoungeSpace.s16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "${country.flag}  ${country.name}", color = colors.textPrimary, style = LoungeType.body)
        }
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "Photos are edited from your profile tab.",
            color = colors.textSecondary,
            style = LoungeType.caption,
        )
        Spacer(Modifier.height(LoungeSpace.s24))
        saveError?.let { message ->
            Text(text = message, color = colors.danger, style = LoungeType.caption)
            Spacer(Modifier.height(LoungeSpace.s8))
        }
        PrimaryButton(
            text = if (saving) "Saving..." else "Save",
            onClick = {
                if (saving) return@PrimaryButton
                saving = true
                saveError = null
                scope.launch {
                    saveError = store.updateProfile(name.trim(), gender, country)
                    saving = false
                    if (saveError == null) onBack()
                }
            },
            enabled = name.trim().length >= 2 && !saving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    BottomSheet(visible = open, onDismiss = { open = false }) {
        LoungeTextField(value = query, onValueChange = { query = it }, placeholder = "Search countries", leadingIcon = AppIcons.Search)
        Spacer(Modifier.height(LoungeSpace.s12))
        LazyColumn(modifier = Modifier.height(320.dp)) {
            items(Countries.filter { query.isBlank() || it.name.contains(query, true) }, key = { it.name }) { item: Country ->
                Text(
                    text = "${item.flag}  ${item.name}",
                    color = colors.textPrimary,
                    style = LoungeType.body,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            country = item
                            open = false
                        }
                        .padding(vertical = 12.dp),
                )
            }
        }
    }
}

@Composable
fun PrivacyScreen(
    store: AppStore,
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    onOpenPage: (String) -> Unit,
) {
    val colors = LoungeTheme.colors
    var delete by remember { mutableStateOf(false) }
    var logout by remember { mutableStateOf(false) }
    var switchGoogle by remember { mutableStateOf(false) }
    var linking by remember { mutableStateOf(false) }
    var accountError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    val blocked = store.blockedIds.mapNotNull { store.person(it) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(LoungeSpace.s20),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircleButton(AppIcons.ChevronLeft, onBack, contentDescription = "Back", size = 40.dp)
            Spacer(Modifier.size(LoungeSpace.s12))
            Text(text = "Account & privacy", color = colors.textPrimary, style = LoungeType.h2)
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        Text(text = "Blocked", color = colors.textPrimary, style = LoungeType.title)
        Spacer(Modifier.height(LoungeSpace.s8))
        if (blocked.isEmpty()) {
            Text(text = "No one is blocked.", color = colors.textSecondary, style = LoungeType.body)
        } else {
            blocked.forEach { person ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = person.name, color = colors.textPrimary, style = LoungeType.body, modifier = Modifier.weight(1f))
                    GhostButton(text = "Unblock", onClick = { store.unblock(person.id) })
                }
            }
        }
        Spacer(Modifier.height(LoungeSpace.s16))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clipSafe()
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.card),
        ) {
            SettingsRow(
                AppIcons.User,
                if (store.isGoogleLinked) "Google account linked" else if (linking) "Linking Google..." else "Link Google account",
                subtitle = if (store.isGoogleLinked) "You can recover this profile" else "Keep the same profile on a new phone",
                onClick = {
                    if (store.isGoogleLinked || linking) return@SettingsRow
                    linking = true
                    accountError = null
                    scope.launch {
                        when (val result = store.linkGoogle(activity)) {
                            LinkGoogleResult.Linked -> Unit
                            LinkGoogleResult.Collision -> switchGoogle = true
                            LinkGoogleResult.Cancelled -> Unit
                            is LinkGoogleResult.Failed -> accountError = result.message
                        }
                        linking = false
                    }
                },
                showDivider = true,
            )
            SettingsRow(AppIcons.Flag, "Privacy policy", onClick = { onOpenPage("privacy") }, showDivider = true)
            SettingsRow(AppIcons.Ban, "Child safety standards", onClick = { onOpenPage("safety") }, showDivider = true)
            SettingsRow(AppIcons.Flag, "Account deletion page", onClick = { onOpenPage("delete") }, showDivider = true)
            SettingsRow(AppIcons.Trash, "Delete account", subtitle = "Removes this device profile", onClick = { delete = true }, showDivider = true)
            SettingsRow(AppIcons.Close, "Log out", subtitle = "Clears this device and starts again", onClick = { logout = true })
        }
        accountError?.let { message ->
            Spacer(Modifier.height(LoungeSpace.s12))
            Text(text = message, color = colors.danger, style = LoungeType.body)
        }
    }
    ModalDialog(
        visible = delete,
        title = "Delete account?",
        body = "This signs you out on this device and clears your name, photos, and chats stored here. You can also follow the account deletion page if you are not in the app.",
        confirmLabel = "Delete",
        destructive = true,
        onConfirm = {
            scope.launch {
                try {
                    store.deleteAccount()
                    delete = false
                    onDeleted()
                } catch (_: Exception) {
                    delete = false
                    accountError = "Couldn't delete the account. Try again."
                }
            }
        },
        onDismiss = { delete = false },
    )
    ModalDialog(
        visible = logout,
        title = "Log out?",
        body = if (store.isGoogleLinked) {
            "You can sign back in with Google on this phone or another one."
        } else {
            "Your account is not linked to Google. Logging out will lose it permanently."
        },
        confirmLabel = "Log out",
        destructive = true,
        onConfirm = {
            scope.launch {
                try {
                    store.logOut()
                    logout = false
                    onDeleted()
                } catch (_: Exception) {
                    logout = false
                    accountError = "Couldn't log out. Try again."
                }
            }
        },
        onDismiss = { logout = false },
    )
    ModalDialog(
        visible = switchGoogle,
        title = "Google account",
        body = "This Google account already has a profile. Sign in to it instead?",
        confirmLabel = "Sign in",
        onConfirm = {
            switchGoogle = false
            scope.launch {
                try {
                    store.useExistingGoogleAccount()
                    onDeleted()
                } catch (_: Exception) {
                    accountError = "Couldn't sign in to that Google account."
                }
            }
        },
        onDismiss = { switchGoogle = false },
    )
}

@Composable
fun HelpScreen(
    onBack: () -> Unit,
    onOpenPage: (String) -> Unit,
) {
    val colors = LoungeTheme.colors
    var topic by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LoungeSpace.s20),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircleButton(AppIcons.ChevronLeft, onBack, contentDescription = "Back", size = 40.dp)
            Spacer(Modifier.size(LoungeSpace.s12))
            Text(text = "Help & support", color = colors.textPrimary, style = LoungeType.h2)
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clipSafe()
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.card),
        ) {
            SettingsRow(AppIcons.Flag, "Safety tips", onClick = { topic = "safety" }, showDivider = true)
            SettingsRow(AppIcons.Message, "Support", subtitle = "hello@larkisedosti.app", onClick = { onOpenPage("support") }, showDivider = true)
            SettingsRow(AppIcons.Flag, "Privacy policy", onClick = { onOpenPage("privacy") }, showDivider = true)
            SettingsRow(AppIcons.Ban, "Child safety standards", onClick = { onOpenPage("safety") })
        }
    }
    val body = when (topic) {
        "safety" -> "Meet in public if you ever move off the app. Don't share passwords or payment codes. Block anyone who pressures you. Report and Block are on profiles, chats, and calls."
        else -> ""
    }
    ModalDialog(
        visible = topic != null,
        title = "Support",
        body = body,
        confirmLabel = "Close",
        onConfirm = { topic = null },
        onDismiss = { topic = null },
    )
}

private fun Modifier.clipSafe(): Modifier = this.clip(LoungeShape.card)

private fun formatDateOfBirth(iso: String): String {
    val parts = iso.split("-")
    if (parts.size != 3) return iso.ifBlank { "Not set" }
    val month = parts[1].toIntOrNull() ?: return iso
    val names = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val label = names.getOrNull(month - 1) ?: return iso
    return "${parts[2].toIntOrNull() ?: parts[2]} $label ${parts[0]}"
}
