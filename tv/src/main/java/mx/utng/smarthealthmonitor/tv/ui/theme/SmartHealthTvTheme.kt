package mx.utng.smarthealthmonitor.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TvDarkColors = darkColorScheme(
    primary        = Color(0xFF1B4F8A),
    secondary      = Color(0xFFD4860A),
    background     = Color(0xFF0D1117),
    surface        = Color(0xFF1A2233),
    error          = Color(0xFFB3261E),
    onPrimary      = Color.White,
    onSecondary    = Color.White,
    onBackground   = Color.White,
    onSurface      = Color.White,
)

@Composable
fun SmartHealthTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TvDarkColors,
        content     = content
    )
}
