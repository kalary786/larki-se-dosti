package com.larkisedosti.app.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Outline icons drawn at 1.75 stroke, in the Lucide style.
 * Tint them with [androidx.compose.material3.Icon]; do not use emoji as UI icons.
 */
object AppIcons {
    val Plus = icon("M12 5v14M5 12h14")
    val Close = icon("M18 6L6 18M6 6l12 12")
    val ChevronRight = icon("M9 18l6-6-6-6", autoMirror = true)
    val Check = icon("M20 6L9 17l-5-5")
    val Search = icon(
        "M11 19a8 8 0 1 0 0-16 8 8 0 0 0 0 16",
        "M21 21l-4.3-4.3",
    )
    val Heart = icon(
        "M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z",
    )
    val User = icon(
        "M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2",
        "M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0",
    )
    val UserPlus = icon(
        "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2",
        "M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8",
        "M19 8v6",
        "M16 11h6",
    )
    val Message = icon("M7.9 20A9 9 0 1 0 4 16.1L2 22Z")
    val Bell = icon(
        "M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9",
        "M10.3 21a1.94 1.94 0 0 0 3.4 0",
    )
    val BellOff = icon(
        "M13.73 21a2 2 0 0 1-3.46 0",
        "M18.63 13A17.9 17.9 0 0 1 18 8",
        "M6.26 6.26A5.86 5.86 0 0 0 6 8c0 7-3 9-3 9h14",
        "M18 8a6 6 0 0 0-9.33-5",
        "M2 2l20 20",
    )
    val Sliders = icon(
        "M4 21v-7M4 10V3M12 21v-9M12 8V3M20 21v-5M20 12V3M2 14h4M10 8h4M18 16h4",
    )
    val Settings = icon(
        "M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z",
        "M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0",
    )
    val Coin = icon(
        "M12 21a9 9 0 1 0 0-18 9 9 0 0 0 0 18",
        "M12 16.5a4.5 4.5 0 1 0 0-9 4.5 4.5 0 0 0 0 9",
    )
    val Image = icon(
        "M3 7a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z",
        "M8.5 10.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3",
        "M21 15l-5-5L5 21",
    )
    val Inbox = icon(
        "M22 12h-6l-2 3H10l-2-3H2",
        "M5.45 5.11 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z",
    )
    val Moon = icon("M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9z")
    val Sun = icon(
        "M12 3v2M12 19v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M3 12h2M19 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42",
        "M16 12a4 4 0 1 1-8 0 4 4 0 0 1 8 0",
    )
    val Compass = icon(
        "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20",
        "M16.24 7.76 14.12 14.12 7.76 16.24 9.88 9.88Z",
    )
    val Users = icon(
        "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2",
        "M13 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0",
        "M22 21v-2a4 4 0 0 0-3-3.87",
        "M16 3.13a4 4 0 0 1 0 7.75",
    )
    val ChevronLeft = icon("M15 18l-6-6 6-6", autoMirror = true)
    val Phone = icon(
        "M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.12.9.3 1.77.54 2.61a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.84.24 1.71.42 2.61.54A2 2 0 0 1 22 16.92z",
    )
    val Video = icon(
        "M15 10l4.55-2.28A1 1 0 0 1 21 8.62v6.76a1 1 0 0 1-1.45.9L15 14v-4z",
        "M3 8a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z",
    )
    val Mic = icon(
        "M12 2a3 3 0 0 0-3 3v7a3 3 0 0 0 6 0V5a3 3 0 0 0-3-3z",
        "M19 10v2a7 7 0 0 1-14 0v-2",
        "M12 19v3",
    )
    val MicOff = icon(
        "M9 9v3a3 3 0 0 0 5.12 2.12M15 9.34V5a3 3 0 0 0-5.94-.6",
        "M17 16.95A7 7 0 0 1 5 12v-2",
        "M12 19v3",
        "M3 3l18 18",
    )
    val Volume = icon(
        "M11 5L6 9H2v6h4l5 4V5z",
        "M15.54 8.46a5 5 0 0 1 0 7.07",
        "M19.07 4.93a10 10 0 0 1 0 14.14",
    )
    val VolumeOff = icon(
        "M11 5L6 9H2v6h4l5 4V5z",
        "M22 9l-6 6",
        "M16 9l6 6",
    )
    val Camera = icon(
        "M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3l-2.5-3z",
        "M12 17a4 4 0 1 0 0-8 4 4 0 0 0 0 8",
    )
    val Gift = icon(
        "M20 12v10H4V12",
        "M2 7h20v5H2z",
        "M12 22V7",
        "M12 7H7.5a2.5 2.5 0 0 1 0-5C11 2 12 7 12 7z",
        "M12 7h4.5a2.5 2.5 0 0 0 0-5C13 2 12 7 12 7z",
    )
    val Crown = icon(
        "M3 17h18",
        "M4 17l2.2-8.2L12 13l5.8-4.2L20 17",
        "M5 17v3h14v-3",
    )
    val Star = icon("M12 2.5l2.7 6.1 6.6.6-5 4.4 1.5 6.4L12 16.8 6.2 20l1.5-6.4-5-4.4 6.6-.6z")
    val Diamond = icon("M6 3h12l4 7-10 11L2 10z")
    val Rose = icon(
        "M12 14a4.5 4.5 0 1 0 0-9 4.5 4.5 0 0 0 0 9",
        "M12 14v8",
        "M12 18c-2.2 0-3.2-1.2-3.2-2.2",
        "M12 16.5c2.2 0 3.2-1.1 3.2-2.1",
    )
    val Bouquet = icon(
        "M8 11a2.2 2.2 0 1 0 0-4.4A2.2 2.2 0 0 0 8 11",
        "M12 10a2.2 2.2 0 1 0 0-4.4A2.2 2.2 0 0 0 12 10",
        "M16 11a2.2 2.2 0 1 0 0-4.4A2.2 2.2 0 0 0 16 11",
        "M12 10v11",
        "M8 11v6",
        "M16 11v6",
    )
    val Send = icon(
        "M22 2L11 13",
        "M22 2l-7 20-4-9-9-4 20-7z",
    )
    val Smile = icon(
        "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20",
        "M8 14s1.5 2 4 2 4-2 4-2",
        "M9 9h.01",
        "M15 9h.01",
    )
    val MoreVertical = icon(
        "M12 6.2a.9.9 0 1 0 0-1.8.9.9 0 0 0 0 1.8",
        "M12 12.9a.9.9 0 1 0 0-1.8.9.9 0 0 0 0 1.8",
        "M12 19.6a.9.9 0 1 0 0-1.8.9.9 0 0 0 0 1.8",
    )
    val Copy = icon(
        "M8 8h11a1 1 0 0 1 1 1v11a1 1 0 0 1-1 1H8a1 1 0 0 1-1-1V9a1 1 0 0 1 1-1z",
        "M4 16V5a1 1 0 0 1 1-1h11",
    )
    val Flag = icon(
        "M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z",
        "M4 22V4",
    )
    val Ban = icon(
        "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20",
        "M5 5l14 14",
    )
    val Trash = icon(
        "M3 6h18",
        "M8 6V4h8v2",
        "M19 6l-1 14H6L5 6",
        "M10 11v6",
        "M14 11v6",
    )
    val MapPin = icon(
        "M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0",
        "M15 10a3 3 0 1 1-6 0 3 3 0 0 1 6 0",
    )
}

private fun icon(vararg paths: String, autoMirror: Boolean = false): ImageVector {
    val builder = ImageVector.Builder(
        name = "LoungeIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = autoMirror,
    )
    val stroke = SolidColor(Color.Black)
    paths.forEach { data ->
        builder.addPath(
            pathData = PathParser().parsePathString(data).toNodes(),
            stroke = stroke,
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
    return builder.build()
}
