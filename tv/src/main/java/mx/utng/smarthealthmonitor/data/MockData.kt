package mx.utng.smarthealthmonitor.data

import mx.utng.smarthealthmonitor.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(id = 2, valorBpm = 72,  hora = "08:00 AM"),
        LecturaFC(id = 3, valorBpm = 68,  hora = "10:00 AM"),
        LecturaFC(id = 4, valorBpm = 91,  hora = "12:00 PM"),
        LecturaFC(id = 5, valorBpm = 110, hora = "02:00 PM"),
        LecturaFC(id = 6, valorBpm = 75,  hora = "04:00 PM"),
        LecturaFC(id = 7, valorBpm = 130, hora = "06:00 PM")
    )
}
