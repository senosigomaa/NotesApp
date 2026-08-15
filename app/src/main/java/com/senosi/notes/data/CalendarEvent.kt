package com.senosi.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val description: String = "",

    val category: String = "Personal",

    val color: String = "#7C5CFC",

    val eventDate: String,

    val eventTime: String,

    val isReminderEnabled: Boolean = false,

    val updatedAt: Long = System.currentTimeMillis()
)