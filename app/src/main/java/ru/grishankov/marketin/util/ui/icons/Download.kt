package ru.grishankov.marketin.util.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Download: ImageVector
    get() {
        if (_icon != null) {
            return _icon!!
        }
        _icon = Builder(name = "Vector", defaultWidth = 24.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 24.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF9B51E0)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(19.35f, 6.04f)
                curveTo(18.67f, 2.59f, 15.64f, 0.0f, 12.0f, 0.0f)
                curveTo(9.11f, 0.0f, 6.6f, 1.64f, 5.35f, 4.04f)
                curveTo(2.34f, 4.36f, 0.0f, 6.91f, 0.0f, 10.0f)
                curveTo(0.0f, 13.31f, 2.69f, 16.0f, 6.0f, 16.0f)
                horizontalLineTo(19.0f)
                curveTo(21.76f, 16.0f, 24.0f, 13.76f, 24.0f, 11.0f)
                curveTo(24.0f, 8.36f, 21.95f, 6.22f, 19.35f, 6.04f)
                close()
                moveTo(17.0f, 9.0f)
                lineTo(12.0f, 14.0f)
                lineTo(7.0f, 9.0f)
                horizontalLineTo(10.0f)
                verticalLineTo(5.0f)
                horizontalLineTo(14.0f)
                verticalLineTo(9.0f)
                horizontalLineTo(17.0f)
                close()
            }
        }
        .build()
        return _icon!!
    }

private var _icon: ImageVector? = null
