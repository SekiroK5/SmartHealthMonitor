package mx.utng.smarthealthmonitor.data.db

// Copia local en el módulo tv — mismos campos que la entity del módulo app
// Se usa mientras tv no depende directamente de :app
data class LecturaFC(
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val hora: String = java.text.SimpleDateFormat(
        "HH:mm", java.util.Locale.getDefault())
        .format(java.util.Date()),
    val esNormal: Boolean = valorBpm in 60..100,
    val dispositivo: String = "tv",
    val sincronizado: Boolean = false
)
