package com.mdrlzy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = OnDark,

    primaryContainer = BluePrimaryContainer,
    onPrimaryContainer = OnDark,

    secondary = BlueSecondary,
    onSecondary = OnDark,

    secondaryContainer = BlueSecondaryContainer,
    onSecondaryContainer = OnDark,

    background = AppBackground,
    onBackground = OnDark,

    surface = AppSurface,
    onSurface = OnDark,

    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = OnDarkMuted,

    outline = OutlineLight,
    outlineVariant = DividerLight,

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    inverseSurface = Color(0xFFEAF3FF),
    inverseOnSurface = Color(0xFF102A43),
    inversePrimary = Color(0xFF8CC8FF),

    scrim = Color(0x66000000)
)

@Composable
fun RBKWeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      shapes = Shapes,
      content = content,
    )
}