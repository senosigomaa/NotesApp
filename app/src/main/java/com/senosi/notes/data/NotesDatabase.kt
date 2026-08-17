package com.senosi.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Note::class,
        CalendarEvent::class
    ],
    version = 3,
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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }

        /**
         * Version 1 -> 2
         *
         * Adds calendar_events table.
         */
        private val MIGRATION_1_2 =
            object : Migration(1, 2) {

                override fun migrate(
                    database: SupportSQLiteDatabase
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

        /**
         * Version 2 -> 3
         *
         * Adds deletedAt to notes.
         *
         * Existing deleted notes use updatedAt
         * as their initial deleted date.
         */
        private val MIGRATION_2_3 =
            object : Migration(2, 3) {

                override fun migrate(
                    database: SupportSQLiteDatabase
                ) {

                    database.execSQL(
                        """
                        ALTER TABLE notes
                        ADD COLUMN deletedAt INTEGER
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        UPDATE notes
                        SET deletedAt = updatedAt
                        WHERE isDeleted = 1
                        AND deletedAt IS NULL
                        """.trimIndent()
                    )
                }
            }
    }
}