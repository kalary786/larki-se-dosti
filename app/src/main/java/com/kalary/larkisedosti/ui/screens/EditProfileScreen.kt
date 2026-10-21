package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kalary.larkisedosti.data.Gender
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.CountryPickerSheet
import com.kalary.larkisedosti.ui.components.FilterChip
import com.kalary.larkisedosti.ui.components.LarkiField
import com.kalary.larkisedosti.ui.components.PhotoSlot
import com.kalary.larkisedosti.ui.components.PrimaryButton
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.components.SecondaryButton
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets

@Composable
fun EditProfileScreen(onBack: () -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    var name by remember { mutableStateOf(app.myName) }
    var gender by remember { mutableStateOf(app.myGender) }
    var showCountries by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenHeader("Edit Profile", onBack)
        Column(Modifier.padding(Space.s16), verticalArrangement = Arrangement.spacedBy(Space.s16)) {
            LarkiField(name, { name = it.take(24) }, placeholder = "Name")
            Text("Gender", style = AppType.title, color = colors.textPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
                Gender.entries.forEach { option ->
                    FilterChip(option.label, gender == option, { gender = option })
                }
            }
            Text("Country", style = AppType.title, color = colors.textPrimary)
            SecondaryButton(
                "${app.myCountry.flag}  ${app.myCountry.name}",
                { showCountries = true },
                Modifier.fillMaxWidth(),
            )
            Text("Photos", style = AppType.title, color = colors.textPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
                app.photoFilled.forEachIndexed { index, filled ->
                    PhotoSlot(filled, index + 4, { app.togglePhoto(index) }, Modifier.weight(1f))
                }
            }
            PrimaryButton(
                "Save",
                {
                    app.myName = name.trim().ifBlank { app.myName }
                    app.myGender = gender
                    app.snackbar = "Profile updated"
                    onBack()
                },
                Modifier.fillMaxWidth(),
                enabled = name.trim().length >= 2,
            )
        }
    }
    if (showCountries) {
        CountryPickerSheet(
            onDismiss = { showCountries = false },
            onPick = { app.myCountry = it },
        )
    }
}
