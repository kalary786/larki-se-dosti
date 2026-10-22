package com.kalary.larkisedosti.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Outline icons from the Lucide set, stroke 1.75.
// ISC License. Copyright (c) Lucide Contributors. https://lucide.dev
object Lucide {

    val ArrowLeft: ImageVector = ImageVector.Builder(
        name = "ArrowLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 19f)
                lineTo(5f, 12f)
                lineTo(12f, 5f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19f, 12f)
                horizontalLineTo(5f)
        }
    }.build()

    val BadgeCheck: ImageVector = ImageVector.Builder(
        name = "BadgeCheck",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(3.85f, 8.62f)
                arcTo(4f, 4f, 0f, false, true, 8.63f, 3.85f)
                arcTo(4f, 4f, 0f, false, true, 15.37f, 3.85f)
                arcTo(4f, 4f, 0f, false, true, 20.15f, 8.63f)
                arcTo(4f, 4f, 0f, false, true, 20.15f, 15.37f)
                arcTo(4f, 4f, 0f, false, true, 15.38f, 20.15f)
                arcTo(4f, 4f, 0f, false, true, 8.63f, 20.15f)
                arcTo(4f, 4f, 0f, false, true, 3.85f, 15.38f)
                arcTo(4f, 4f, 0f, false, true, 3.85f, 8.62f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 9f)
                lineTo(10.5f, 14.5f)
                lineTo(8f, 12f)
        }
    }.build()

    val Ban: ImageVector = ImageVector.Builder(
        name = "Ban",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4.929f, 4.929f)
                lineTo(19.07f, 19.071f)
        }
    }.build()

    val Bell: ImageVector = ImageVector.Builder(
        name = "Bell",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(10.268f, 21f)
                arcTo(2f, 2f, 0f, false, false, 13.732f, 21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(3.262f, 15.326f)
                arcTo(1f, 1f, 0f, false, false, 4f, 17f)
                horizontalLineTo(20f)
                arcTo(1f, 1f, 0f, false, false, 20.74f, 15.327f)
                curveTo(19.41f, 13.956f, 18f, 12.499f, 18f, 8f)
                arcTo(6f, 6f, 0f, false, false, 6f, 8f)
                curveTo(6f, 12.499f, 4.589f, 13.956f, 3.262f, 15.326f)
        }
    }.build()

    val Camera: ImageVector = ImageVector.Builder(
        name = "Camera",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13.997f, 4f)
                arcTo(2f, 2f, 0f, false, true, 15.757f, 5.05f)
                lineTo(16.243f, 5.95f)
                arcTo(2f, 2f, 0f, false, false, 18.003f, 7f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 9f)
                verticalLineTo(18f)
                arcTo(2f, 2f, 0f, false, true, 20f, 20f)
                horizontalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 2f, 18f)
                verticalLineTo(9f)
                arcTo(2f, 2f, 0f, false, true, 4f, 7f)
                horizontalLineTo(5.997f)
                arcTo(2f, 2f, 0f, false, false, 7.756f, 5.952f)
                lineTo(8.245f, 5.048f)
                arcTo(2f, 2f, 0f, false, true, 10.004f, 4f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 13f)
                arcTo(3f, 3f, 0f, false, true, 9f, 13f)
                arcTo(3f, 3f, 0f, false, true, 15f, 13f)
                close()
        }
    }.build()

    val CameraPlus: ImageVector = ImageVector.Builder(
        name = "CameraPlus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13.997f, 4f)
                arcTo(2f, 2f, 0f, false, true, 15.757f, 5.05f)
                lineTo(16.243f, 5.95f)
                arcTo(2f, 2f, 0f, false, false, 18.003f, 7f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 9f)
                verticalLineTo(18f)
                arcTo(2f, 2f, 0f, false, true, 20f, 20f)
                horizontalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 2f, 18f)
                verticalLineTo(9f)
                arcTo(2f, 2f, 0f, false, true, 4f, 7f)
                horizontalLineTo(5.997f)
                arcTo(2f, 2f, 0f, false, false, 7.756f, 5.952f)
                lineTo(8.245f, 5.048f)
                arcTo(2f, 2f, 0f, false, true, 10.004f, 4f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 10f)
                verticalLineTo(16f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9f, 13f)
                horizontalLineTo(15f)
        }
    }.build()

    val CameraOff: ImageVector = ImageVector.Builder(
        name = "CameraOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(14.564f, 14.558f)
                arcTo(3f, 3f, 0f, true, true, 10.442f, 10.437f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2f, 2f)
                lineTo(22f, 22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 20f)
                horizontalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 2f, 18f)
                verticalLineTo(9f)
                arcTo(2f, 2f, 0f, false, true, 4f, 7f)
                horizontalLineTo(5.997f)
                arcTo(2f, 2f, 0f, false, false, 6.816f, 6.825f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9.695f, 4.024f)
                arcTo(2f, 2f, 0f, false, true, 10.004f, 4f)
                horizontalLineTo(13.997f)
                arcTo(2f, 2f, 0f, false, true, 15.757f, 5.05f)
                lineTo(16.243f, 5.95f)
                arcTo(2f, 2f, 0f, false, false, 18.003f, 7f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 9f)
                verticalLineTo(16.344f)
        }
    }.build()

    val Check: ImageVector = ImageVector.Builder(
        name = "Check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 6f)
                lineTo(9f, 17f)
                lineTo(4f, 12f)
        }
    }.build()

    val ChevronLeft: ImageVector = ImageVector.Builder(
        name = "ChevronLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 18f)
                lineTo(9f, 12f)
                lineTo(15f, 6f)
        }
    }.build()

    val ChevronRight: ImageVector = ImageVector.Builder(
        name = "ChevronRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9f, 18f)
                lineTo(15f, 12f)
                lineTo(9f, 6f)
        }
    }.build()

    val CircleQuestionMark: ImageVector = ImageVector.Builder(
        name = "CircleQuestionMark",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9.09f, 9f)
                arcTo(3f, 3f, 0f, false, true, 14.92f, 10f)
                curveTo(14.92f, 12f, 11.92f, 13f, 11.92f, 13f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 17f)
                horizontalLineTo(12.01f)
        }
    }.build()

    val CircleUser: ImageVector = ImageVector.Builder(
        name = "CircleUser",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 10f)
                arcTo(3f, 3f, 0f, false, true, 9f, 10f)
                arcTo(3f, 3f, 0f, false, true, 15f, 10f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(7f, 20.662f)
                verticalLineTo(19f)
                arcTo(2f, 2f, 0f, false, true, 9f, 17f)
                horizontalLineTo(15f)
                arcTo(2f, 2f, 0f, false, true, 17f, 19f)
                verticalLineTo(20.662f)
        }
    }.build()

    val Coins: ImageVector = ImageVector.Builder(
        name = "Coins",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13.744f, 17.736f)
                arcTo(6f, 6f, 0f, true, true, 6.264f, 10.256f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 6f)
                horizontalLineTo(16f)
                verticalLineTo(10f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(6.134f, 14.768f)
                lineTo(7f, 14.268f)
                lineTo(9f, 17.732f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 8f)
                arcTo(6f, 6f, 0f, false, true, 10f, 8f)
                arcTo(6f, 6f, 0f, false, true, 22f, 8f)
                close()
        }
    }.build()

    val Compass: ImageVector = ImageVector.Builder(
        name = "Compass",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16.24f, 7.76f)
                lineTo(14.436f, 13.171f)
                arcTo(2f, 2f, 0f, false, true, 13.171f, 14.436f)
                lineTo(7.76f, 16.24f)
                lineTo(9.564f, 10.829f)
                arcTo(2f, 2f, 0f, false, true, 10.829f, 9.564f)
                close()
        }
    }.build()

    val Copy: ImageVector = ImageVector.Builder(
        name = "Copy",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(10f, 8f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 10f)
                verticalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 20f, 22f)
                horizontalLineTo(10f)
                arcTo(2f, 2f, 0f, false, true, 8f, 20f)
                verticalLineTo(10f)
                arcTo(2f, 2f, 0f, false, true, 10f, 8f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4f, 16f)
                curveTo(2.9f, 16f, 2f, 15.1f, 2f, 14f)
                verticalLineTo(4f)
                curveTo(2f, 2.9f, 2.9f, 2f, 4f, 2f)
                horizontalLineTo(14f)
                curveTo(15.1f, 2f, 16f, 2.9f, 16f, 4f)
        }
    }.build()

    val Crown: ImageVector = ImageVector.Builder(
        name = "Crown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(11.562f, 3.266f)
                arcTo(0.5f, 0.5f, 0f, false, true, 12.438f, 3.266f)
                lineTo(15.39f, 8.87f)
                arcTo(1f, 1f, 0f, false, false, 16.906f, 9.164f)
                lineTo(21.183f, 5.5f)
                arcTo(0.5f, 0.5f, 0f, false, true, 21.981f, 6.019f)
                lineTo(19.147f, 16.265f)
                arcTo(1f, 1f, 0f, false, true, 18.191f, 16.999f)
                horizontalLineTo(5.81f)
                arcTo(1f, 1f, 0f, false, true, 4.853f, 16.265f)
                lineTo(2.02f, 6.02f)
                arcTo(0.5f, 0.5f, 0f, false, true, 2.818f, 5.501f)
                lineTo(7.094f, 9.165f)
                arcTo(1f, 1f, 0f, false, false, 8.61f, 8.871f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(5f, 21f)
                horizontalLineTo(19f)
        }
    }.build()

    val EllipsisVertical: ImageVector = ImageVector.Builder(
        name = "EllipsisVertical",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 12f)
                arcTo(1f, 1f, 0f, false, true, 11f, 12f)
                arcTo(1f, 1f, 0f, false, true, 13f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 5f)
                arcTo(1f, 1f, 0f, false, true, 11f, 5f)
                arcTo(1f, 1f, 0f, false, true, 13f, 5f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 19f)
                arcTo(1f, 1f, 0f, false, true, 11f, 19f)
                arcTo(1f, 1f, 0f, false, true, 13f, 19f)
                close()
        }
    }.build()

    val Flag: ImageVector = ImageVector.Builder(
        name = "Flag",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4f, 22f)
                verticalLineTo(4f)
                arcTo(1f, 1f, 0f, false, true, 4.4f, 3.2f)
                arcTo(6f, 6f, 0f, false, true, 8f, 2f)
                curveTo(11f, 2f, 13f, 4f, 15.333f, 4f)
                quadTo(17.333f, 4f, 18.4f, 3.2f)
                arcTo(1f, 1f, 0f, false, true, 20f, 4f)
                verticalLineTo(14f)
                arcTo(1f, 1f, 0f, false, true, 19.6f, 14.8f)
                arcTo(6f, 6f, 0f, false, true, 16f, 16f)
                curveTo(13f, 16f, 11f, 14f, 8f, 14f)
                arcTo(6f, 6f, 0f, false, false, 4f, 15.528f)
        }
    }.build()

    val Gem: ImageVector = ImageVector.Builder(
        name = "Gem",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(10.5f, 3f)
                lineTo(8f, 9f)
                lineTo(12f, 22f)
                lineTo(16f, 9f)
                lineTo(13.5f, 3f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(17f, 3f)
                arcTo(2f, 2f, 0f, false, true, 18.6f, 3.8f)
                lineTo(21.6f, 7.8f)
                arcTo(2f, 2f, 0f, false, true, 21.613f, 10.182f)
                lineTo(13.623f, 21.168f)
                arcTo(2f, 2f, 0f, false, true, 10.376f, 21.168f)
                lineTo(2.386f, 10.182f)
                arcTo(2f, 2f, 0f, false, true, 2.4f, 7.8f)
                lineTo(5.398f, 3.803f)
                arcTo(2f, 2f, 0f, false, true, 7f, 3f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2f, 9f)
                horizontalLineTo(22f)
        }
    }.build()

    val Gift: ImageVector = ImageVector.Builder(
        name = "Gift",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 7f)
                verticalLineTo(21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 11f)
                verticalLineTo(19f)
                arcTo(2f, 2f, 0f, false, true, 18f, 21f)
                horizontalLineTo(6f)
                arcTo(2f, 2f, 0f, false, true, 4f, 19f)
                verticalLineTo(11f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(7.5f, 7f)
                arcTo(1f, 1f, 0f, false, true, 7.5f, 2f)
                arcTo(4.8f, 8f, 0f, false, true, 12f, 7f)
                arcTo(4.8f, 8f, 0f, false, true, 16.5f, 2f)
                arcTo(1f, 1f, 0f, false, true, 16.5f, 7f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4f, 7f)
                horizontalLineTo(20f)
                arcTo(1f, 1f, 0f, false, true, 21f, 8f)
                verticalLineTo(10f)
                arcTo(1f, 1f, 0f, false, true, 20f, 11f)
                horizontalLineTo(4f)
                arcTo(1f, 1f, 0f, false, true, 3f, 10f)
                verticalLineTo(8f)
                arcTo(1f, 1f, 0f, false, true, 4f, 7f)
                close()
        }
    }.build()

    val Globe: ImageVector = ImageVector.Builder(
        name = "Globe",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 2f)
                arcTo(14.5f, 14.5f, 0f, false, false, 12f, 22f)
                arcTo(14.5f, 14.5f, 0f, false, false, 12f, 2f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2f, 12f)
                horizontalLineTo(22f)
        }
    }.build()

    val Heart: ImageVector = ImageVector.Builder(
        name = "Heart",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2f, 9.5f)
                arcTo(5.5f, 5.5f, 0f, false, true, 11.591f, 5.824f)
                arcTo(0.56f, 0.56f, 0f, false, false, 12.409f, 5.824f)
                arcTo(5.49f, 5.49f, 0f, false, true, 22f, 9.5f)
                curveTo(22f, 11.79f, 20.5f, 13.5f, 19f, 15f)
                lineTo(13.508f, 20.313f)
                arcTo(2f, 2f, 0f, false, true, 10.508f, 20.332f)
                lineTo(5f, 15f)
                curveTo(3.5f, 13.5f, 2f, 11.8f, 2f, 9.5f)
        }
    }.build()

    val ImagePlus: ImageVector = ImageVector.Builder(
        name = "ImagePlus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 5f)
                horizontalLineTo(22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19f, 2f)
                verticalLineTo(8f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(21f, 11.5f)
                verticalLineTo(19f)
                arcTo(2f, 2f, 0f, false, true, 19f, 21f)
                horizontalLineTo(5f)
                arcTo(2f, 2f, 0f, false, true, 3f, 19f)
                verticalLineTo(5f)
                arcTo(2f, 2f, 0f, false, true, 5f, 3f)
                horizontalLineTo(12.5f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(21f, 15f)
                lineTo(17.914f, 11.914f)
                arcTo(2f, 2f, 0f, false, false, 15.086f, 11.914f)
                lineTo(6f, 21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(11f, 9f)
                arcTo(2f, 2f, 0f, false, true, 7f, 9f)
                arcTo(2f, 2f, 0f, false, true, 11f, 9f)
                close()
        }
    }.build()

    val Info: ImageVector = ImageVector.Builder(
        name = "Info",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 16f)
                verticalLineTo(12f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 8f)
                horizontalLineTo(12.01f)
        }
    }.build()

    val Mail: ImageVector = ImageVector.Builder(
        name = "Mail",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 7f)
                lineTo(13.009f, 12.727f)
                arcTo(2f, 2f, 0f, false, true, 11f, 12.727f)
                lineTo(2f, 7f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4f, 4f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 6f)
                verticalLineTo(18f)
                arcTo(2f, 2f, 0f, false, true, 20f, 20f)
                horizontalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 2f, 18f)
                verticalLineTo(6f)
                arcTo(2f, 2f, 0f, false, true, 4f, 4f)
                close()
        }
    }.build()

    val MapPin: ImageVector = ImageVector.Builder(
        name = "MapPin",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 10f)
                curveTo(20f, 14.993f, 14.461f, 20.193f, 12.601f, 21.799f)
                arcTo(1f, 1f, 0f, false, true, 11.399f, 21.799f)
                curveTo(9.539f, 20.193f, 4f, 14.993f, 4f, 10f)
                arcTo(8f, 8f, 0f, false, true, 20f, 10f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 10f)
                arcTo(3f, 3f, 0f, false, true, 9f, 10f)
                arcTo(3f, 3f, 0f, false, true, 15f, 10f)
                close()
        }
    }.build()

    val MessageCircle: ImageVector = ImageVector.Builder(
        name = "MessageCircle",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2.992f, 16.342f)
                arcTo(2f, 2f, 0f, false, true, 3.086f, 17.509f)
                lineTo(2.021f, 20.799f)
                arcTo(1f, 1f, 0f, false, false, 3.257f, 21.967f)
                lineTo(6.67f, 20.969f)
                arcTo(2f, 2f, 0f, false, true, 7.769f, 21.061f)
                arcTo(10f, 10f, 0f, true, false, 2.992f, 16.342f)
        }
    }.build()

    val Mic: ImageVector = ImageVector.Builder(
        name = "Mic",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 19f)
                verticalLineTo(22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19f, 10f)
                verticalLineTo(12f)
                arcTo(7f, 7f, 0f, false, true, 5f, 12f)
                verticalLineTo(10f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 2f)
                horizontalLineTo(12f)
                arcTo(3f, 3f, 0f, false, true, 15f, 5f)
                verticalLineTo(12f)
                arcTo(3f, 3f, 0f, false, true, 12f, 15f)
                horizontalLineTo(12f)
                arcTo(3f, 3f, 0f, false, true, 9f, 12f)
                verticalLineTo(5f)
                arcTo(3f, 3f, 0f, false, true, 12f, 2f)
                close()
        }
    }.build()

    val MicOff: ImageVector = ImageVector.Builder(
        name = "MicOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 19f)
                verticalLineTo(22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 9.34f)
                verticalLineTo(5f)
                arcTo(3f, 3f, 0f, false, false, 9.32f, 3.67f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16.95f, 16.95f)
                arcTo(7f, 7f, 0f, false, true, 5f, 12f)
                verticalLineTo(10f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(18.89f, 13.23f)
                arcTo(7f, 7f, 0f, false, false, 19f, 12f)
                verticalLineTo(10f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2f, 2f)
                lineTo(22f, 22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9f, 9f)
                verticalLineTo(12f)
                arcTo(3f, 3f, 0f, false, false, 14.12f, 14.12f)
        }
    }.build()

    val Moon: ImageVector = ImageVector.Builder(
        name = "Moon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20.985f, 12.486f)
                arcTo(9f, 9f, 0f, true, true, 11.512f, 3.014f)
                curveTo(11.917f, 2.992f, 12.129f, 3.474f, 11.914f, 3.817f)
                arcTo(6f, 6f, 0f, false, false, 20.182f, 12.085f)
                curveTo(20.526f, 11.87f, 21.007f, 12.081f, 20.985f, 12.486f)
        }
    }.build()

    val Pencil: ImageVector = ImageVector.Builder(
        name = "Pencil",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(21.174f, 6.812f)
                arcTo(1f, 1f, 0f, false, false, 17.188f, 2.825f)
                lineTo(3.842f, 16.174f)
                arcTo(2f, 2f, 0f, false, false, 3.342f, 17.004f)
                lineTo(2.021f, 21.356f)
                arcTo(0.5f, 0.5f, 0f, false, false, 2.644f, 21.978f)
                lineTo(6.997f, 20.658f)
                arcTo(2f, 2f, 0f, false, false, 7.827f, 20.161f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 5f)
                lineTo(19f, 9f)
        }
    }.build()

    val Phone: ImageVector = ImageVector.Builder(
        name = "Phone",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13.832f, 16.568f)
                arcTo(1f, 1f, 0f, false, false, 15.045f, 16.265f)
                lineTo(15.4f, 15.8f)
                arcTo(2f, 2f, 0f, false, true, 17f, 15f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 17f)
                verticalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 20f, 22f)
                arcTo(18f, 18f, 0f, false, true, 2f, 4f)
                arcTo(2f, 2f, 0f, false, true, 4f, 2f)
                horizontalLineTo(7f)
                arcTo(2f, 2f, 0f, false, true, 9f, 4f)
                verticalLineTo(7f)
                arcTo(2f, 2f, 0f, false, true, 8.2f, 8.6f)
                lineTo(7.732f, 8.951f)
                arcTo(1f, 1f, 0f, false, false, 7.44f, 10.184f)
                arcTo(14f, 14f, 0f, false, false, 13.832f, 16.568f)
        }
    }.build()

    val Play: ImageVector = ImageVector.Builder(
        name = "Play",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(5f, 5f)
                arcTo(2f, 2f, 0f, false, true, 8.008f, 3.272f)
                lineTo(20.005f, 10.27f)
                arcTo(2f, 2f, 0f, false, true, 20.008f, 13.728f)
                lineTo(8.008f, 20.728f)
                arcTo(2f, 2f, 0f, false, true, 5f, 19f)
                close()
        }
    }.build()

    val Plus: ImageVector = ImageVector.Builder(
        name = "Plus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(5f, 12f)
                horizontalLineTo(19f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 5f)
                verticalLineTo(19f)
        }
    }.build()

    val Radar: ImageVector = ImageVector.Builder(
        name = "Radar",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19.07f, 4.93f)
                arcTo(10f, 10f, 0f, false, false, 6.99f, 3.34f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4f, 6f)
                horizontalLineTo(4.01f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2.29f, 9.62f)
                arcTo(10f, 10f, 0f, true, false, 21.31f, 8.35f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16.24f, 7.76f)
                arcTo(6f, 6f, 0f, true, false, 8.23f, 16.67f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 18f)
                horizontalLineTo(12.01f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(17.99f, 11.66f)
                arcTo(6f, 6f, 0f, false, true, 15.77f, 16.67f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(14f, 12f)
                arcTo(2f, 2f, 0f, false, true, 10f, 12f)
                arcTo(2f, 2f, 0f, false, true, 14f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13.41f, 10.59f)
                lineTo(19.07f, 4.93f)
        }
    }.build()

    val Search: ImageVector = ImageVector.Builder(
        name = "Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(21f, 21f)
                lineTo(16.66f, 16.66f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19f, 11f)
                arcTo(8f, 8f, 0f, false, true, 3f, 11f)
                arcTo(8f, 8f, 0f, false, true, 19f, 11f)
                close()
        }
    }.build()

    val Send: ImageVector = ImageVector.Builder(
        name = "Send",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(14.536f, 21.686f)
                arcTo(0.5f, 0.5f, 0f, false, false, 15.473f, 21.662f)
                lineTo(21.973f, 2.662f)
                arcTo(0.496f, 0.496f, 0f, false, false, 21.338f, 2.027f)
                lineTo(2.338f, 8.527f)
                arcTo(0.5f, 0.5f, 0f, false, false, 2.314f, 9.464f)
                lineTo(10.244f, 12.644f)
                arcTo(2f, 2f, 0f, false, true, 11.356f, 13.754f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(21.854f, 2.147f)
                lineTo(10.914f, 13.086f)
        }
    }.build()

    val Settings: ImageVector = ImageVector.Builder(
        name = "Settings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9.671f, 4.136f)
                arcTo(2.34f, 2.34f, 0f, false, true, 14.33f, 4.136f)
                arcTo(2.34f, 2.34f, 0f, false, false, 17.649f, 6.051f)
                arcTo(2.34f, 2.34f, 0f, false, true, 19.979f, 10.084f)
                arcTo(2.34f, 2.34f, 0f, false, false, 19.979f, 13.915f)
                arcTo(2.34f, 2.34f, 0f, false, true, 17.649f, 17.948f)
                arcTo(2.34f, 2.34f, 0f, false, false, 14.33f, 19.863f)
                arcTo(2.34f, 2.34f, 0f, false, true, 9.671f, 19.863f)
                arcTo(2.34f, 2.34f, 0f, false, false, 6.351f, 17.948f)
                arcTo(2.34f, 2.34f, 0f, false, true, 4.021f, 13.915f)
                arcTo(2.34f, 2.34f, 0f, false, false, 4.021f, 10.084f)
                arcTo(2.34f, 2.34f, 0f, false, true, 6.35f, 6.051f)
                arcTo(2.34f, 2.34f, 0f, false, false, 9.669f, 4.136f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 12f)
                arcTo(3f, 3f, 0f, false, true, 9f, 12f)
                arcTo(3f, 3f, 0f, false, true, 15f, 12f)
                close()
        }
    }.build()

    val Shield: ImageVector = ImageVector.Builder(
        name = "Shield",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 13f)
                curveTo(20f, 18f, 16.5f, 20.5f, 12.34f, 21.95f)
                arcTo(1f, 1f, 0f, false, true, 11.67f, 21.94f)
                curveTo(7.5f, 20.5f, 4f, 18f, 4f, 13f)
                verticalLineTo(6f)
                arcTo(1f, 1f, 0f, false, true, 5f, 5f)
                curveTo(7f, 5f, 9.5f, 3.8f, 11.24f, 2.28f)
                arcTo(1.17f, 1.17f, 0f, false, true, 12.76f, 2.28f)
                curveTo(14.51f, 3.81f, 17f, 5f, 19f, 5f)
                arcTo(1f, 1f, 0f, false, true, 20f, 6f)
                close()
        }
    }.build()

    val Smile: ImageVector = ImageVector.Builder(
        name = "Smile",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(8f, 14f)
                curveTo(8f, 14f, 9.5f, 16f, 12f, 16f)
                curveTo(14.5f, 16f, 16f, 14f, 16f, 14f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(9f, 9f)
                lineTo(9.01f, 9f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15f, 9f)
                lineTo(15.01f, 9f)
        }
    }.build()

    val Sparkles: ImageVector = ImageVector.Builder(
        name = "Sparkles",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(11.017f, 2.814f)
                arcTo(1f, 1f, 0f, false, true, 12.983f, 2.814f)
                lineTo(14.034f, 8.372f)
                arcTo(2f, 2f, 0f, false, false, 15.628f, 9.966f)
                lineTo(21.186f, 11.017f)
                arcTo(1f, 1f, 0f, false, true, 21.186f, 12.983f)
                lineTo(15.628f, 14.034f)
                arcTo(2f, 2f, 0f, false, false, 14.034f, 15.628f)
                lineTo(12.983f, 21.186f)
                arcTo(1f, 1f, 0f, false, true, 11.017f, 21.186f)
                lineTo(9.966f, 15.628f)
                arcTo(2f, 2f, 0f, false, false, 8.372f, 14.034f)
                lineTo(2.814f, 12.983f)
                arcTo(1f, 1f, 0f, false, true, 2.814f, 11.017f)
                lineTo(8.372f, 9.966f)
                arcTo(2f, 2f, 0f, false, false, 9.966f, 8.372f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 2f)
                verticalLineTo(6f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 4f)
                horizontalLineTo(18f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(6f, 20f)
                arcTo(2f, 2f, 0f, false, true, 2f, 20f)
                arcTo(2f, 2f, 0f, false, true, 6f, 20f)
                close()
        }
    }.build()

    val Star: ImageVector = ImageVector.Builder(
        name = "Star",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(11.525f, 2.295f)
                arcTo(0.53f, 0.53f, 0f, false, true, 12.475f, 2.295f)
                lineTo(14.785f, 6.974f)
                arcTo(2.123f, 2.123f, 0f, false, false, 16.38f, 8.134f)
                lineTo(21.546f, 8.89f)
                arcTo(0.53f, 0.53f, 0f, false, true, 21.84f, 9.794f)
                lineTo(18.104f, 13.432f)
                arcTo(2.123f, 2.123f, 0f, false, false, 17.493f, 15.31f)
                lineTo(18.375f, 20.45f)
                arcTo(0.53f, 0.53f, 0f, false, true, 17.604f, 21.01f)
                lineTo(12.986f, 18.582f)
                arcTo(2.122f, 2.122f, 0f, false, false, 11.013f, 18.582f)
                lineTo(6.396f, 21.01f)
                arcTo(0.53f, 0.53f, 0f, false, true, 5.626f, 20.45f)
                lineTo(6.507f, 15.311f)
                arcTo(2.122f, 2.122f, 0f, false, false, 5.896f, 13.432f)
                lineTo(2.16f, 9.795f)
                arcTo(0.53f, 0.53f, 0f, false, true, 2.454f, 8.889f)
                lineTo(7.619f, 8.134f)
                arcTo(2.122f, 2.122f, 0f, false, false, 9.216f, 6.974f)
                close()
        }
    }.build()

    val Sun: ImageVector = ImageVector.Builder(
        name = "Sun",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 12f)
                arcTo(4f, 4f, 0f, false, true, 8f, 12f)
                arcTo(4f, 4f, 0f, false, true, 16f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 2f)
                verticalLineTo(4f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 20f)
                verticalLineTo(22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4.93f, 4.93f)
                lineTo(6.34f, 6.34f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(17.66f, 17.66f)
                lineTo(19.07f, 19.07f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(2f, 12f)
                horizontalLineTo(4f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 12f)
                horizontalLineTo(22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(6.34f, 17.66f)
                lineTo(4.93f, 19.07f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19.07f, 4.93f)
                lineTo(17.66f, 6.34f)
        }
    }.build()

    val Trash: ImageVector = ImageVector.Builder(
        name = "Trash",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(10f, 11f)
                verticalLineTo(17f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(14f, 11f)
                verticalLineTo(17f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19f, 6f)
                verticalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 17f, 22f)
                horizontalLineTo(7f)
                arcTo(2f, 2f, 0f, false, true, 5f, 20f)
                verticalLineTo(6f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(3f, 6f)
                horizontalLineTo(21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(8f, 6f)
                verticalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 10f, 2f)
                horizontalLineTo(14f)
                arcTo(2f, 2f, 0f, false, true, 16f, 4f)
                verticalLineTo(6f)
        }
    }.build()

    val UserCheck: ImageVector = ImageVector.Builder(
        name = "UserCheck",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 11f)
                lineTo(18f, 13f)
                lineTo(22f, 9f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 21f)
                verticalLineTo(19f)
                arcTo(4f, 4f, 0f, false, false, 12f, 15f)
                horizontalLineTo(6f)
                arcTo(4f, 4f, 0f, false, false, 2f, 19f)
                verticalLineTo(21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 7f)
                arcTo(4f, 4f, 0f, false, true, 5f, 7f)
                arcTo(4f, 4f, 0f, false, true, 13f, 7f)
                close()
        }
    }.build()

    val UserPlus: ImageVector = ImageVector.Builder(
        name = "UserPlus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 21f)
                verticalLineTo(19f)
                arcTo(4f, 4f, 0f, false, false, 12f, 15f)
                horizontalLineTo(6f)
                arcTo(4f, 4f, 0f, false, false, 2f, 19f)
                verticalLineTo(21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 7f)
                arcTo(4f, 4f, 0f, false, true, 5f, 7f)
                arcTo(4f, 4f, 0f, false, true, 13f, 7f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19f, 8f)
                lineTo(19f, 14f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 11f)
                lineTo(16f, 11f)
        }
    }.build()

    val UserX: ImageVector = ImageVector.Builder(
        name = "UserX",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 21f)
                verticalLineTo(19f)
                arcTo(4f, 4f, 0f, false, false, 12f, 15f)
                horizontalLineTo(6f)
                arcTo(4f, 4f, 0f, false, false, 2f, 19f)
                verticalLineTo(21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 7f)
                arcTo(4f, 4f, 0f, false, true, 5f, 7f)
                arcTo(4f, 4f, 0f, false, true, 13f, 7f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(17f, 8f)
                lineTo(22f, 13f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 8f)
                lineTo(17f, 13f)
        }
    }.build()

    val Users: ImageVector = ImageVector.Builder(
        name = "Users",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 21f)
                verticalLineTo(19f)
                arcTo(4f, 4f, 0f, false, false, 12f, 15f)
                horizontalLineTo(6f)
                arcTo(4f, 4f, 0f, false, false, 2f, 19f)
                verticalLineTo(21f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 3.128f)
                arcTo(4f, 4f, 0f, false, true, 16f, 10.872f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 21f)
                verticalLineTo(19f)
                arcTo(4f, 4f, 0f, false, false, 19f, 15.13f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13f, 7f)
                arcTo(4f, 4f, 0f, false, true, 5f, 7f)
                arcTo(4f, 4f, 0f, false, true, 13f, 7f)
                close()
        }
    }.build()

    val Video: ImageVector = ImageVector.Builder(
        name = "Video",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 13f)
                lineTo(21.223f, 16.482f)
                arcTo(0.5f, 0.5f, 0f, false, false, 22f, 16.066f)
                verticalLineTo(7.87f)
                arcTo(0.5f, 0.5f, 0f, false, false, 21.248f, 7.438f)
                lineTo(16f, 10.5f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(4f, 6f)
                horizontalLineTo(14f)
                arcTo(2f, 2f, 0f, false, true, 16f, 8f)
                verticalLineTo(16f)
                arcTo(2f, 2f, 0f, false, true, 14f, 18f)
                horizontalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 2f, 16f)
                verticalLineTo(8f)
                arcTo(2f, 2f, 0f, false, true, 4f, 6f)
                close()
        }
    }.build()

    val Volume2: ImageVector = ImageVector.Builder(
        name = "Volume2",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(11f, 4.702f)
                arcTo(0.705f, 0.705f, 0f, false, false, 9.797f, 4.204f)
                lineTo(6.413f, 7.587f)
                arcTo(1.4f, 1.4f, 0f, false, true, 5.416f, 8f)
                horizontalLineTo(3f)
                arcTo(1f, 1f, 0f, false, false, 2f, 9f)
                verticalLineTo(15f)
                arcTo(1f, 1f, 0f, false, false, 3f, 16f)
                horizontalLineTo(5.416f)
                arcTo(1.4f, 1.4f, 0f, false, true, 6.413f, 16.413f)
                lineTo(9.796f, 19.797f)
                arcTo(0.705f, 0.705f, 0f, false, false, 11f, 19.298f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 9f)
                arcTo(5f, 5f, 0f, false, true, 16f, 15f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(19.364f, 18.364f)
                arcTo(9f, 9f, 0f, false, false, 19.364f, 5.636f)
        }
    }.build()

    val VolumeX: ImageVector = ImageVector.Builder(
        name = "VolumeX",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(11f, 4.702f)
                arcTo(0.7f, 0.7f, 0f, false, false, 9.797f, 4.204f)
                lineTo(6.413f, 7.587f)
                arcTo(1.4f, 1.4f, 0f, false, true, 5.416f, 8f)
                horizontalLineTo(3f)
                arcTo(1f, 1f, 0f, false, false, 2f, 9f)
                verticalLineTo(15f)
                arcTo(1f, 1f, 0f, false, false, 3f, 16f)
                horizontalLineTo(5.416f)
                arcTo(1.4f, 1.4f, 0f, false, true, 6.413f, 16.413f)
                lineTo(9.796f, 19.797f)
                arcTo(0.7f, 0.7f, 0f, false, false, 11f, 19.298f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16.5f, 14.5f)
                lineTo(21.5f, 9.5f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16.5f, 9.5f)
                lineTo(21.5f, 14.5f)
        }
    }.build()

    val X: ImageVector = ImageVector.Builder(
        name = "X",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(18f, 6f)
                lineTo(6f, 18f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(6f, 6f)
                lineTo(18f, 18f)
        }
    }.build()

    val Zap: ImageVector = ImageVector.Builder(
        name = "Zap",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(15.914f, 4f)
                arcTo(1.5f, 1.5f, 0f, false, false, 13.44f, 2.439f)
                lineTo(4.44f, 11.439f)
                arcTo(1.5f, 1.5f, 0f, false, false, 5.5f, 14f)
                horizontalLineTo(9.502f)
                arcTo(0.5f, 0.5f, 0f, false, true, 9.973f, 14.666f)
                lineTo(8.086f, 20f)
                arcTo(1.5f, 1.5f, 0f, false, false, 10.561f, 21.56f)
                lineTo(19.561f, 12.56f)
                arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 10f)
                horizontalLineTo(14.503f)
                arcTo(0.5f, 0.5f, 0f, false, true, 14.031f, 9.333f)
                close()
        }
    }.build()

    val Clock: ImageVector = ImageVector.Builder(
        name = "Clock",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 12f)
                arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(12f, 6f)
                verticalLineTo(12f)
                lineTo(16f, 14f)
        }
    }.build()

    val FileText: ImageVector = ImageVector.Builder(
        name = "FileText",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(6f, 22f)
                arcTo(2f, 2f, 0f, false, true, 4f, 20f)
                verticalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 6f, 2f)
                horizontalLineTo(14f)
                arcTo(2.4f, 2.4f, 0f, false, true, 15.704f, 2.706f)
                lineTo(19.292f, 6.294f)
                arcTo(2.4f, 2.4f, 0f, false, true, 20f, 8f)
                verticalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 18f, 22f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(14f, 2f)
                verticalLineTo(7f)
                arcTo(1f, 1f, 0f, false, false, 15f, 8f)
                horizontalLineTo(20f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(10f, 9f)
                horizontalLineTo(8f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 13f)
                horizontalLineTo(8f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 17f)
                horizontalLineTo(8f)
        }
    }.build()

    val MessagesSquare: ImageVector = ImageVector.Builder(
        name = "MessagesSquare",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 10f)
                arcTo(2f, 2f, 0f, false, true, 14f, 12f)
                horizontalLineTo(6.828f)
                arcTo(2f, 2f, 0f, false, false, 5.414f, 12.586f)
                lineTo(3.212f, 14.788f)
                arcTo(0.71f, 0.71f, 0f, false, true, 2f, 14.286f)
                verticalLineTo(4f)
                arcTo(2f, 2f, 0f, false, true, 4f, 2f)
                horizontalLineTo(14f)
                arcTo(2f, 2f, 0f, false, true, 16f, 4f)
                close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(20f, 9f)
                arcTo(2f, 2f, 0f, false, true, 22f, 11f)
                verticalLineTo(21.286f)
                arcTo(0.71f, 0.71f, 0f, false, true, 20.788f, 21.788f)
                lineTo(18.586f, 19.586f)
                arcTo(2f, 2f, 0f, false, false, 17.172f, 19f)
                horizontalLineTo(10f)
                arcTo(2f, 2f, 0f, false, true, 8f, 17f)
                verticalLineTo(16f)
        }
    }.build()

    val PhoneIncoming: ImageVector = ImageVector.Builder(
        name = "PhoneIncoming",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(16f, 2f)
                verticalLineTo(8f)
                horizontalLineTo(22f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(22f, 2f)
                lineTo(16f, 8f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.75f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
                moveTo(13.832f, 16.568f)
                arcTo(1f, 1f, 0f, false, false, 15.045f, 16.265f)
                lineTo(15.4f, 15.8f)
                arcTo(2f, 2f, 0f, false, true, 17f, 15f)
                horizontalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 22f, 17f)
                verticalLineTo(20f)
                arcTo(2f, 2f, 0f, false, true, 20f, 22f)
                arcTo(18f, 18f, 0f, false, true, 2f, 4f)
                arcTo(2f, 2f, 0f, false, true, 4f, 2f)
                horizontalLineTo(7f)
                arcTo(2f, 2f, 0f, false, true, 9f, 4f)
                verticalLineTo(7f)
                arcTo(2f, 2f, 0f, false, true, 8.2f, 8.6f)
                lineTo(7.732f, 8.951f)
                arcTo(1f, 1f, 0f, false, false, 7.44f, 10.184f)
                arcTo(14f, 14f, 0f, false, false, 13.832f, 16.568f)
        }
    }.build()

}
