package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.Country
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun CountryPickerSheet(
    onDismiss: () -> Unit,
    onPick: (Country) -> Unit,
) {
    val colors = LarkiTheme.colors
    var query by remember { mutableStateOf("") }
    val shown = MockData.countries.filter {
        query.isBlank() || it.name.contains(query, ignoreCase = true)
    }
    AppBottomSheet(onDismiss) {
        Column(Modifier.padding(horizontal = Space.s16).padding(bottom = Space.s16)) {
            Text("Choose a country", style = AppType.h2, color = colors.textPrimary)
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Search countries",
                modifier = Modifier.padding(vertical = Space.s12),
            )
            LazyColumn(Modifier.heightIn(max = 420.dp)) {
                items(shown, key = { it.name }) { country ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                onPick(country)
                                onDismiss()
                            }
                            .padding(vertical = Space.s12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(country.flag, style = AppType.title)
                        Text(
                            country.name,
                            style = AppType.body,
                            color = colors.textPrimary,
                            modifier = Modifier.padding(start = Space.s12),
                        )
                    }
                }
            }
        }
    }
}
