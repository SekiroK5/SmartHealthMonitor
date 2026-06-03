package mx.utng.smarthealthmonitor.wear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.*
import kotlinx.coroutines.launch

import androidx.activity.result.contract.ActivityResultContracts

class WearMainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.BODY_SENSORS] == true
        if (granted) {
            registrarHealthService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permisos de sensor
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BODY_SENSORS,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            )
        } else {
            registrarHealthService()
        }

        setContent {
            WearApp()
        }
    }

    private fun registrarHealthService() {
        lifecycleScope.launch {
            HealthDataService.registrar(applicationContext)
        }
    }
}

@Composable
fun WearApp() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SmartHealth",
                    style = MaterialTheme.typography.title1
                )
                Text(
                    text = "Monitoreando FC...",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}
