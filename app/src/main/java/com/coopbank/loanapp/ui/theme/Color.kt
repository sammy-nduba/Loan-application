package com.coopbank.loanapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Pink = Color(0XFFF28482)
val Green = Color ( 0xFF84A59D)
val Yellow = Color ( 0xFFF7EDE2)
val YellowLight = Color(0xFFFFFF2)
val Dark = Color (0xFF3D405B)
val LightGrey = Color(0xFFF5F5F5)
val DarkSurface = Color(0xFF121212)
val OnDarkSurface = Color(0xFFE1E1E1)

@Immutable
data class AppColors(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val secondarySurface: Color,
    val onSecondarySurface: Color,
    val regularSurface: Color,
    val onRegularSurface: Color,
    val actionSurface: Color,
    val onActionSurface: Color,
    val highlightSurface: Color,
    val onHighlightSurface: Color,
    val isDark: Boolean
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        secondarySurface = Color.Unspecified,
        onSecondarySurface = Color.Unspecified,
        regularSurface = Color.Unspecified,
        onRegularSurface = Color.Unspecified,
        actionSurface = Color.Unspecified,
        onActionSurface = Color.Unspecified,
        highlightSurface = Color.Unspecified,
        onHighlightSurface = Color.Unspecified,
        isDark = false
    )
}

val extendedColors = AppColors (
    background = Color.White,
    onBackground = Dark,
    surface = Color.White,
    onSurface = Dark,
    secondarySurface = Pink,
    onSecondarySurface = Color.White,
    regularSurface = YellowLight,
    onRegularSurface = Dark,
    actionSurface = Yellow,
    onActionSurface = Pink,
    highlightSurface = Green,
    onHighlightSurface = Color.White,
    isDark = false
)

val darkExtendedColors = AppColors(
    background = DarkSurface,
    onBackground = OnDarkSurface,
    surface = Color(0xFF1E1E1E),
    onSurface = OnDarkSurface,
    secondarySurface = Pink.copy(alpha = 0.8f),
    onSecondarySurface = Color.White,
    regularSurface = Color(0xFF2C2C2C),
    onRegularSurface = OnDarkSurface,
    actionSurface = Color(0xFF333333),
    onActionSurface = Pink,
    highlightSurface = Green.copy(alpha = 0.8f),
    onHighlightSurface = Color.White,
    isDark = true
)
