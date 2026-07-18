package mx.utng.smarthealthmonitor.data.db

import android.content.Context
import androidx.room.*

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LecturaFC::class],
    version  = 2,
    exportSchema = false
)
abstract class SmartHealthDB : RoomDatabase() {
    abstract fun lecturaDao(): LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDB? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE lecturas_fc ADD COLUMN dispositivo TEXT NOT NULL DEFAULT 'app'")
                db.execSQL("ALTER TABLE lecturas_fc ADD COLUMN sincronizado INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): SmartHealthDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDB::class.java,
                    "smarthealthmonitor_db"
                )
                .addMigrations(MIGRATION_1_2)
                .build().also { INSTANCE = it }
            }
        }
    }
}
