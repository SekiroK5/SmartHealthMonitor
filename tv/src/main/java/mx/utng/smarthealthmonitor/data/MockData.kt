package mx.utng.smarthealthmonitor.data

import mx.utng.smarthealthmonitor.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(id=2, valorBpm=72, hora="10:00 AM"),
        LecturaFC(id=3, valorBpm=120, hora="11:30 AM")
    )
}
