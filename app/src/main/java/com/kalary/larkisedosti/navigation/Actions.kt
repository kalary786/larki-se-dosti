package com.kalary.larkisedosti.navigation

import androidx.navigation.NavHostController
import com.kalary.larkisedosti.data.AppState
import com.kalary.larkisedosti.data.CoinGate
import com.kalary.larkisedosti.data.GateAction
import com.kalary.larkisedosti.data.Gift
import com.kalary.larkisedosti.data.MockData

fun startOutgoingCall(app: AppState, nav: NavHostController, personId: String, video: Boolean) {
    val id = app.baseId(personId)
    if (app.spend(MockData.CALL_COST)) {
        nav.navigate(Routes.call(id, video, incoming = false))
    } else {
        app.gate = CoinGate(
            need = MockData.CALL_COST,
            pill = "This call costs 10 coins",
            action = GateAction.Call(id, video, incoming = false),
        )
    }
}

fun requestGift(app: AppState, personId: String, gift: Gift) {
    val id = app.baseId(personId)
    if (app.spend(gift.cost)) {
        app.deliverGift(id, gift)
    } else {
        app.gate = CoinGate(
            need = gift.cost,
            pill = "This gift costs ${gift.cost} coins",
            action = GateAction.GiftSend(id, gift.id),
        )
    }
}
