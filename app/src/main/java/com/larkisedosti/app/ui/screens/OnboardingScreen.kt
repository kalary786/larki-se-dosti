package com.larkisedosti.app.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.larkisedosti.app.data.GoogleAuthResult
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
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import java.util.Calendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    store: AppStore,
    onFinished: () -> Unit,
) {
    val colors = LoungeTheme.colors
    var step by remember { mutableIntStateOf(0) }
    val today = remember { Calendar.getInstance() }
    var birthDay by remember { mutableIntStateOf(1) }
    var birthMonth by remember { mutableIntStateOf(1) }
    var birthYear by remember { mutableIntStateOf(today.get(Calendar.YEAR) - 21) }
    val age = ageYears(birthDay, birthMonth, birthYear)
    var blocked by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var usernameStatus by remember { mutableStateOf(UsernameStatus.Idle) }
    var gender by remember { mutableStateOf<Gender?>(null) }
    var country by remember { mutableStateOf<Country?>(null) }
    var photo by remember { mutableStateOf<String?>(null) }
    var terms by remember { mutableStateOf(false) }
    var adultConfirmed by remember { mutableStateOf(false) }
    var countryOpen by remember { mutableStateOf(false) }
    var countryQuery by remember { mutableStateOf("") }
    var policy by remember { mutableStateOf<String?>(null) }
    var saving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }
    var googleMessage by remember { mutableStateOf<String?>(null) }
    var googleBusy by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    val picker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        photo = uri?.toString()
    }
    LaunchedEffect(username) {
        val clean = username.trim()
        if (clean.isEmpty()) {
            usernameStatus = UsernameStatus.Idle
            return@LaunchedEffect
        }
        if (!clean.matches(usernamePattern)) {
            usernameStatus = UsernameStatus.Invalid
            return@LaunchedEffect
        }
        usernameStatus = UsernameStatus.Checking
        delay(500)
        usernameStatus = try {
            if (store.usernameAvailable(clean)) UsernameStatus.Available else UsernameStatus.Taken
        } catch (_: Exception) {
            UsernameStatus.Error
        }
    }
    BackHandler(enabled = step > 0 && !blocked && !saving) { step -= 1 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = LoungeSpace.s20, vertical = LoungeSpace.s16),
    ) {
        Text(
            text = "Step ${step + 1} of 7",
            color = colors.textSecondary,
            style = LoungeType.caption,
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(7) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(LoungeShape.chip)
                        .background(if (index <= step) colors.primary else colors.surface),
                )
            }
        }
        Spacer(Modifier.height(LoungeSpace.s24))
        if (blocked) {
            Text(text = "You need to be 18", color = colors.textPrimary, style = LoungeType.h1)
            Spacer(Modifier.height(LoungeSpace.s12))
            Text(
                text = "You are $age. Larki Se Dosti is only for adults. Come back when you are 18 or older.",
                color = colors.textSecondary,
                style = LoungeType.body,
            )
            Spacer(Modifier.weight(1f))
            SecondaryButton(
                text = "Change date of birth",
                onClick = { blocked = false },
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            when (step) {
                0 -> BirthStep(
                    day = birthDay,
                    month = birthMonth,
                    year = birthYear,
                    age = age,
                    confirmed = adultConfirmed,
                    onDate = { day, month, year ->
                        val maxDay = daysInMonth(month, year)
                        birthDay = day.coerceIn(1, maxDay)
                        birthMonth = month
                        birthYear = year
                        if (ageYears(birthDay, month, year) < 18) adultConfirmed = false
                    },
                    onConfirmed = { adultConfirmed = it },
                )
                1 -> NameStep(name, onName = { name = it })
                2 -> UsernameStep(
                    username = username,
                    status = usernameStatus,
                    onUsername = { typed ->
                        username = typed.lowercase().filter { it.isLetterOrDigit() || it == '_' }.take(20)
                    },
                )
                3 -> GenderStep(gender, onPick = { gender = it })
                4 -> CountryStep(country, onOpen = { countryOpen = true })
                5 -> PhotoStep(photo, onPick = {
                    picker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                })
                else -> TermsStep(
                    checked = terms,
                    onChecked = { terms = it },
                    onOpenTerms = { policy = "terms" },
                    onOpenPrivacy = { policy = "privacy" },
                )
            }
            Spacer(Modifier.weight(1f))
            when (step) {
                0 -> {
                    PrimaryButton(
                        text = "Continue",
                        onClick = { if (age < 18) blocked = true else step = 1 },
                        enabled = (age < 18 || adultConfirmed) && !googleBusy,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(LoungeSpace.s12))
                    Text(
                        text = if (googleBusy) "Opening Google..." else "Already have an account? Sign in with Google",
                        color = colors.primary,
                        style = LoungeType.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !googleBusy) {
                                googleBusy = true
                                googleMessage = null
                                scope.launch {
                                    when (val result = store.signInWithGoogle(activity)) {
                                        is GoogleAuthResult.Ready -> {
                                            if (result.profile?.onboardingCompleted == true) onFinished()
                                        }
                                        GoogleAuthResult.Cancelled -> Unit
                                        is GoogleAuthResult.Failed -> googleMessage = result.message
                                    }
                                    googleBusy = false
                                }
                            },
                    )
                    googleMessage?.let { message ->
                        Spacer(Modifier.height(LoungeSpace.s8))
                        Text(text = message, color = colors.danger, style = LoungeType.caption)
                    }
                }
                1 -> PrimaryButton(
                    text = "Continue",
                    onClick = { step = 2 },
                    enabled = name.trim().length >= 2,
                    modifier = Modifier.fillMaxWidth(),
                )
                2 -> PrimaryButton(
                    text = "Continue",
                    onClick = { step = 3 },
                    enabled = usernameStatus == UsernameStatus.Available,
                    modifier = Modifier.fillMaxWidth(),
                )
                3 -> PrimaryButton(
                    text = "Continue",
                    onClick = { step = 4 },
                    enabled = gender != null,
                    modifier = Modifier.fillMaxWidth(),
                )
                4 -> PrimaryButton(
                    text = "Continue",
                    onClick = { step = 5 },
                    enabled = country != null,
                    modifier = Modifier.fillMaxWidth(),
                )
                5 -> {
                    PrimaryButton(
                        text = "Continue",
                        onClick = { step = 6 },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(LoungeSpace.s8))
                    GhostButton(
                        text = "Skip photo",
                        onClick = { step = 6 },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                else -> {
                    saveError?.let { message ->
                        Text(text = message, color = colors.danger, style = LoungeType.caption)
                        Spacer(Modifier.height(LoungeSpace.s8))
                    }
                    PrimaryButton(
                        text = if (saving) "Saving..." else "Create account",
                        onClick = {
                            if (saving) return@PrimaryButton
                            saving = true
                            saveError = null
                            scope.launch {
                                saveError = store.completeOnboarding(
                                    username = username.trim(),
                                    name = name.trim(),
                                    dateOfBirth = "%04d-%02d-%02d".format(birthYear, birthMonth, birthDay),
                                    gender = gender ?: Gender.Other,
                                    country = country ?: Countries.first(),
                                    photo = photo,
                                )
                                saving = false
                                if (saveError == null) onFinished()
                            }
                        },
                        enabled = terms && !saving && usernameStatus == UsernameStatus.Available,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    BottomSheet(visible = countryOpen, onDismiss = { countryOpen = false }) {
        Text(text = "Country", color = colors.textPrimary, style = LoungeType.h2)
        Spacer(Modifier.height(LoungeSpace.s12))
        LoungeTextField(
            value = countryQuery,
            onValueChange = { countryQuery = it },
            placeholder = "Search countries",
            leadingIcon = AppIcons.Search,
        )
        Spacer(Modifier.height(LoungeSpace.s12))
        val shown = Countries.filter {
            countryQuery.isBlank() || it.name.contains(countryQuery, ignoreCase = true)
        }
        LazyColumn(modifier = Modifier.height(360.dp)) {
            items(shown, key = { it.name }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            country = item
                            countryOpen = false
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = item.flag, style = LoungeType.title)
                    Spacer(Modifier.size(LoungeSpace.s12))
                    Text(text = item.name, color = colors.textPrimary, style = LoungeType.body)
                }
            }
        }
    }

    ModalDialog(
        visible = policy != null,
        title = if (policy == "privacy") "Privacy" else "Terms",
        body = if (policy == "privacy") {
            "We keep your profile on this device. Photos are reviewed before other people can see them. You can block someone or delete the account at any time."
        } else {
            "Be kind, be 18 or older, and don't send anything you wouldn't say in person. We can remove accounts that harass, scam, or pretend to be someone else."
        },
        confirmLabel = "Close",
        dismissLabel = "Back",
        onConfirm = { policy = null },
        onDismiss = { policy = null },
    )
}

@Composable
private fun BirthStep(
    day: Int,
    month: Int,
    year: Int,
    age: Int,
    confirmed: Boolean,
    onDate: (day: Int, month: Int, year: Int) -> Unit,
    onConfirmed: (Boolean) -> Unit,
) {
    val colors = LoungeTheme.colors
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    Text(text = "Date of birth", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s8))
    Text(
        text = "You need to be 18 or older. This account stays on your device. No password.",
        color = colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s24))
    Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
        DobMenu(
            label = "Day",
            value = day.toString(),
            options = (1..daysInMonth(month, year)).map { it to it.toString() },
            onPick = { onDate(it, month, year) },
            modifier = Modifier.weight(1f),
        )
        DobMenu(
            label = "Month",
            value = monthNames[month - 1],
            options = monthNames.mapIndexed { index, name -> (index + 1) to name },
            onPick = { onDate(day, it, year) },
            modifier = Modifier.weight(1.3f),
        )
        DobMenu(
            label = "Year",
            value = year.toString(),
            options = (currentYear - 80..currentYear - 13).reversed().map { it to it.toString() },
            onPick = { onDate(day, month, it) },
            modifier = Modifier.weight(1.1f),
        )
    }
    Spacer(Modifier.height(LoungeSpace.s16))
    Text(
        text = if (age < 18) {
            "You are $age. Larki Se Dosti is only for adults 18 and older."
        } else {
            "Age $age"
        },
        color = if (age < 18) colors.danger else colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s16))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = confirmed && age >= 18,
            onCheckedChange = { onConfirmed(it && age >= 18) },
            enabled = age >= 18,
            colors = CheckboxDefaults.colors(
                checkedColor = colors.primary,
                uncheckedColor = colors.outline,
                checkmarkColor = colors.onPrimary,
                disabledUncheckedColor = colors.outline,
            ),
        )
        Text(
            text = "I confirm I am 18 or older.",
            color = if (age >= 18) colors.textPrimary else colors.textSecondary,
            style = LoungeType.body,
            modifier = Modifier.clickable(enabled = age >= 18) {
                onConfirmed(!(confirmed && age >= 18))
            },
        )
    }
}

@Composable
private fun DobMenu(
    label: String,
    value: String,
    options: List<Pair<Int, String>>,
    onPick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    var open by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(text = label, color = colors.textSecondary, style = LoungeType.caption)
        Spacer(Modifier.height(6.dp))
        Box {
            Text(
                text = value,
                color = colors.textPrimary,
                style = LoungeType.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LoungeShape.input)
                    .background(colors.surface)
                    .border(1.dp, colors.border, LoungeShape.input)
                    .clickable { open = true }
                    .padding(horizontal = 12.dp, vertical = 14.dp),
            )
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onPick(id)
                            open = false
                        },
                    )
                }
            }
        }
    }
}

private val monthNames = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)

private fun daysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

private fun ageYears(day: Int, month: Int, year: Int): Int {
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - year
    val currentMonth = today.get(Calendar.MONTH) + 1
    val currentDay = today.get(Calendar.DAY_OF_MONTH)
    if (currentMonth < month || (currentMonth == month && currentDay < day)) age -= 1
    return age
}

@Composable
private fun NameStep(name: String, onName: (String) -> Unit) {
    val colors = LoungeTheme.colors
    Text(text = "What should we call you?", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s8))
    Text(
        text = "This display name is what people see on your profile card. It does not have to be unique.",
        color = colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s24))
    LoungeTextField(value = name, onValueChange = onName, placeholder = "Display name")
}

@Composable
private fun UsernameStep(
    username: String,
    status: UsernameStatus,
    onUsername: (String) -> Unit,
) {
    val colors = LoungeTheme.colors
    Text(text = "Choose a username", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s8))
    Text(
        text = "3–20 characters. Lowercase letters, numbers, and underscore. It has to start with a letter.",
        color = colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s24))
    LoungeTextField(
        value = username,
        onValueChange = onUsername,
        placeholder = "username",
        capitalization = KeyboardCapitalization.None,
    )
    Spacer(Modifier.height(LoungeSpace.s8))
    val (label, color) = when (status) {
        UsernameStatus.Idle -> "" to colors.textSecondary
        UsernameStatus.Invalid -> "Use 3–20 characters, starting with a letter." to colors.danger
        UsernameStatus.Checking -> "Checking..." to colors.textSecondary
        UsernameStatus.Available -> "Available" to colors.online
        UsernameStatus.Taken -> "Already taken" to colors.danger
        UsernameStatus.Error -> "Couldn't check just now. Try again." to colors.danger
    }
    if (label.isNotEmpty()) {
        Text(text = label, color = color, style = LoungeType.caption)
    }
}

private enum class UsernameStatus { Idle, Invalid, Checking, Available, Taken, Error }

private val usernamePattern = Regex("^[a-z][a-z0-9_]{2,19}$")

@Composable
private fun GenderStep(selected: Gender?, onPick: (Gender) -> Unit) {
    val colors = LoungeTheme.colors
    Text(text = "I am", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s20))
    Gender.entries.forEach { option ->
        val active = option == selected
        Text(
            text = option.label,
            color = if (active) colors.onPrimary else colors.textPrimary,
            style = LoungeType.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LoungeSpace.s8)
                .clip(LoungeShape.card)
                .background(if (active) colors.primary else colors.surface)
                .border(1.dp, if (active) colors.primary else colors.border, LoungeShape.card)
                .clickable { onPick(option) }
                .padding(LoungeSpace.s16),
        )
    }
}

@Composable
private fun CountryStep(country: Country?, onOpen: () -> Unit) {
    val colors = LoungeTheme.colors
    Text(text = "Where are you?", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s8))
    Text(
        text = "People nearby see your country. You can change it later.",
        color = colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s24))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(LoungeShape.input)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.input)
            .clickable(onClick = onOpen)
            .padding(LoungeSpace.s16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = country?.flag ?: "", style = LoungeType.title)
        Spacer(Modifier.size(LoungeSpace.s12))
        Text(
            text = country?.name ?: "Choose a country",
            color = if (country == null) colors.textSecondary else colors.textPrimary,
            style = LoungeType.body,
            modifier = Modifier.weight(1f),
        )
        androidx.compose.material3.Icon(
            imageVector = AppIcons.ChevronRight,
            contentDescription = null,
            tint = colors.textSecondary,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun PhotoStep(photo: String?, onPick: () -> Unit) {
    val colors = LoungeTheme.colors
    Text(text = "Add a photo", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s8))
    Text(
        text = "Optional. It stays hidden until a review is done.",
        color = colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s24))
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(LoungeShape.card)
            .background(colors.surface)
            .border(1.dp, colors.outline, LoungeShape.card)
            .clickable(onClick = onPick),
        contentAlignment = Alignment.Center,
    ) {
        if (photo != null) {
            AsyncImage(
                model = photo,
                contentDescription = "Your photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.material3.Icon(
                    imageVector = AppIcons.Camera,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(Modifier.height(LoungeSpace.s8))
                Text(text = "Upload", color = colors.textSecondary, style = LoungeType.caption)
            }
        }
    }
    if (photo != null) {
        Spacer(Modifier.height(LoungeSpace.s12))
        Text(
            text = "Pending review. Other people won't see it yet.",
            color = colors.textSecondary,
            style = LoungeType.caption,
        )
    }
}

@Composable
private fun TermsStep(
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
) {
    val colors = LoungeTheme.colors
    Text(text = "Before you go in", color = colors.textPrimary, style = LoungeType.h1)
    Spacer(Modifier.height(LoungeSpace.s8))
    Text(
        text = "Sign in stays on this device. There is no password to remember.",
        color = colors.textSecondary,
        style = LoungeType.body,
    )
    Spacer(Modifier.height(LoungeSpace.s24))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = colors.primary,
                uncheckedColor = colors.outline,
                checkmarkColor = colors.onPrimary,
            ),
        )
        Column {
            Text(text = "I accept the terms and privacy note.", color = colors.textPrimary, style = LoungeType.body)
            Row {
                Text(
                    text = "Terms",
                    color = colors.primary,
                    style = LoungeType.caption,
                    modifier = Modifier.clickable(onClick = onOpenTerms),
                )
                Text(text = "  ·  ", color = colors.textSecondary, style = LoungeType.caption)
                Text(
                    text = "Privacy",
                    color = colors.primary,
                    style = LoungeType.caption,
                    modifier = Modifier.clickable(onClick = onOpenPrivacy),
                )
            }
        }
    }
}
