package mx.utng.smarthealthmonitor.data.db

data class LecturaFC(
    val id: Int = 0,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = valorBpm in 60..100
)
