package com.kalary.larkisedosti.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.kalary.larkisedosti.data.Gender
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.ui.components.FilterChip
import com.kalary.larkisedosti.ui.components.GhostButton
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.components.LarkiField
import com.kalary.larkisedosti.ui.components.PhotoSlot
import com.kalary.larkisedosti.ui.components.PrimaryButton
import com.kalary.larkisedosti.ui.components.SearchField
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun OnboardingScreen(onDone: () -> Unit, onLegal: (String) -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val step = app.onboardingStep
    BackHandler(enabled = step > 0) { app.onboardingStep = step - 1 }
    val age = app.ageText.toIntOrNull()
    val underage = age != null && age < 18
    val canContinue = when (step) {
        0 -> age != null && age in 18..99
        1 -> app.draftName.trim().length >= 2
        2 -> app.draftGender != null
        3 -> app.draftCountry != null
        else -> app.termsAccepted
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .tag("screen_onboarding")
            .padding(Space.s16),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (step > 0) {
                IconCircleButton(Lucide.ChevronLeft, "Back", { app.onboardingStep = step - 1 })
            }
            Text(
                "${step + 1} / 5",
                style = AppType.caption,
                color = colors.textSecondary,
                modifier = Modifier.padding(start = Space.s12),
            )
        }
        Text(
            when (step) {
                0 -> "How old are you?"
                1 -> "What should people call you?"
                2 -> "How do you identify?"
                3 -> "Where do you live?"
                else -> "Add a photo"
            },
            style = AppType.h1,
            color = colors.textPrimary,
            modifier = Modifier.padding(top = Space.s24),
        )
        Text(
            when (step) {
                0 -> "No password. This sign-in stays on your device. You need to be 18 or older."
                1 -> "This is the name on your profile."
                4 -> "Optional. Photos stay pending until they are approved."
                else -> "You can change this later."
            },
            style = AppType.body,
            color = colors.textSecondary,
            modifier = Modifier.padding(top = Space.s8, bottom = Space.s20),
        )
        Box(Modifier.weight(1f).fillMaxWidth()) {
            when (step) {
                0 -> AgeStep(underage)
                1 -> LarkiField(
                    value = app.draftName,
                    onValueChange = { app.draftName = it.take(24) },
                    placeholder = "Your name",
                    modifier = Modifier.tag("name_field"),
                )
                2 -> GenderStep()
                3 -> CountryStep()
                else -> PhotoStep(onLegal)
            }
        }
        PrimaryButton(
            text = if (step == 4) "Enter Larki" else "Continue",
            onClick = {
                if (step < 4) app.onboardingStep = step + 1
                else {
                    app.completeOnboarding()
                    onDone()
                }
            },
            enabled = canContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Space.s16)
                .tag("continue_button"),
        )
    }
}

@Composable
private fun AgeStep(underage: Boolean) {
    val app = LocalAppState.current
    val colors = LarkiTheme.colors
    Column {
    LarkiField(
        value = app.ageText,
        onValueChange = { raw -> app.ageText = raw.filter { it.isDigit() }.take(2) },
        placeholder = "Age",
        keyboardType = KeyboardType.Number,
        modifier = Modifier.tag("age_field"),
    )
    if (underage) {
        Text(
            "Larki Se Dosti is for adults 18 and over.",
            style = AppType.body,
            color = colors.danger,
            modifier = Modifier.padding(top = Space.s12),
        )
    }
    }
}

@Composable
private fun GenderStep() {
    val app = LocalAppState.current
    Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
        Gender.entries.forEach { gender ->
            FilterChip(
                text = gender.label,
                selected = app.draftGender == gender,
                onClick = { app.draftGender = gender },
                modifier = Modifier.tag("gender_${gender.name}"),
            )
        }
    }
}

@Composable
private fun CountryStep() {
    val app = LocalAppState.current
    val colors = LarkiTheme.colors
    var query by remember { mutableStateOf("") }
    val shown = MockData.countries.filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
    Column(Modifier.fillMaxSize()) {
    SearchField(query, { query = it }, "Search countries")
    LazyColumn(Modifier.padding(top = Space.s8).weight(1f)) {
        items(shown, key = { it.name }) { country ->
            val selected = app.draftCountry == country
            Row(
                Modifier
                    .fillMaxWidth()
                    .tag("country_${country.name}")
                    .clickable { app.draftCountry = country }
                    .padding(vertical = Space.s12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(country.flag, style = AppType.title)
                Text(
                    country.name,
                    style = AppType.body,
                    color = if (selected) colors.primary else colors.textPrimary,
                    modifier = Modifier.padding(start = Space.s12).weight(1f),
                )
                if (selected) Text("Selected", style = AppType.caption, color = colors.primary)
            }
        }
    }
    }
}

@Composable
private fun PhotoStep(onLegal: (String) -> Unit) {
    val app = LocalAppState.current
    val colors = LarkiTheme.colors
    Column {
    PhotoSlot(
        filled = app.draftPhoto,
        seed = 2,
        onClick = { app.draftPhoto = !app.draftPhoto },
        modifier = Modifier.fillMaxWidth(0.42f),
    )
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Space.s16)) {
        Checkbox(
            checked = app.termsAccepted,
            onCheckedChange = { app.termsAccepted = it },
            modifier = Modifier.tag("terms_checkbox"),
            colors = CheckboxDefaults.colors(
                checkedColor = colors.primary,
                uncheckedColor = colors.textSecondary,
                checkmarkColor = colors.onPrimary,
            ),
        )
        Column(Modifier.weight(1f)) {
            Text("I agree to the Terms and Privacy Policy.", style = AppType.body, color = colors.textPrimary)
            Row {
                GhostButton("Terms", { onLegal("privacy") })
                GhostButton("Privacy", { onLegal("privacy") })
            }
        }
    }
    }
}
