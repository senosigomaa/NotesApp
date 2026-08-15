package com.senosi.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Note::class,
        CalendarEvent::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun calendarEventDao(): CalendarEventDao

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }

        private val MIGRATION_1_2 =
            object : androidx.room.migration.Migration(1, 2) {

                override fun migrate(
                    database: androidx.sqlite.db.SupportSQLiteDatabase
                ) {
                    database.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS calendar_events (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            title TEXT NOT NULL,
                            description TEXT NOT NULL,
                            category TEXT NOT NULL,
                            color TEXT NOT NULL,
                            eventDate TEXT NOT NULL,
                            eventTime TEXT NOT NULL,
                            isReminderEnabled INTEGER NOT NULL,
                            updatedAt INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                }
            }
    }
}