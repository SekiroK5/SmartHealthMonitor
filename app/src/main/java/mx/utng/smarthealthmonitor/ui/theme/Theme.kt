package mx.utng.smarthealthmonitor.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary          = SHPrimary,
    onPrimary        = SHOnPrimary,
    primaryContainer = SHPrimaryContainer,
    secondary        = SHSecondary,
    background       = SHBackground,
    surface          = SHSurface,
    onSurface        = SHOnSurface,
    error            = SHError,              // ← rojo oscuro para texto
    onError          = SHOnError,
    errorContainer   = SHErrorContainer,     // ← rosa claro para fondos
    onErrorContainer = SHOnErrorContainer,
)

private val DarkColorScheme = darkColorScheme(
    primary          = SHPrimaryDark,
    onPrimary        = SHOnPrimaryDark,
    primaryContainer = SHPrimaryContainerDark,
    secondary        = SHSecondaryDark,
    background       = SHBackgroundDark,
    surface          = SHSurfaceDark,
    error            = SHErrorDark,
    onError          = SHOnErrorDark,
    errorContainer   = SHErrorContainerDark,
    onErrorContainer = SHOnErrorContainerDark,
)

@Composable
fun SmartHealthMonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}

@Preview(showBackground = true, name = "Light")
@Preview(
    showBackground = true,
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ThemePreview() {
    SmartHealthMonitorTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text(
                text     = "SmartHealth Monitor",
                style    = MaterialTheme.typography.headlineMedium,
                color    = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}