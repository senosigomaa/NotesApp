package com.senosi.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senosi.notes.data.CalendarEvent
import com.senosi.notes.data.CalendarEventRepository
import com.senosi.notes.data.NotesDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: CalendarEventRepository

    init {
        val database = NotesDatabase.getInstance(application)

        repository = CalendarEventRepository(
            database.calendarEventDao()
        )

        seedEvents()
    }

    fun observeEventsForDate(
        date: String
    ): Flow<List<CalendarEvent>> {
        return repository.observeEventsForDate(date)
    }

    fun observeUpcomingEvents(
        fromDate: String
    ): Flow<List<CalendarEvent>> {
        return repository.observeUpcomingEvents(fromDate)
    }

    fun addEvent(
        title: String,
        description: String,
        category: String,
        color: String,
        eventDate: String,
        eventTime: String,
        isReminderEnabled: Boolean
    ) {
        viewModelScope.launch {
            repository.insert(
                CalendarEvent(
                    title = title,
                    description = description,
                    category = category,
                    color = color,
                    eventDate = eventDate,
                    eventTime = eventTime,
                    isReminderEnabled = isReminderEnabled
                )
            )
        }
    }

    fun updateEvent(
        event: CalendarEvent
    ) {
        viewModelScope.launch {
            repository.update(
                event.copy(
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteEvent(
        event: CalendarEvent
    ) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }

    private fun seedEvents() {
        viewModelScope.launch {

            val today = LocalDate.now()

            val existingEvents = repository
                .observeUpcomingEvents(today.toString())
                .first()

            if (existingEvents.isNotEmpty()) {
                return@launch
            }

            repository.insert(
                CalendarEvent(
                    title = "Design Inspiration",
                    description = "Explore new UI ideas and concepts",
                    category = "Personal",
                    color = "#7C5CFC",
                    eventDate = today.toString(),
                    eventTime = "09:00 AM",
                    isReminderEnabled = true
                )
            )

            repository.insert(
                CalendarEvent(
                    title = "Workout Plan",
                    description = "Chest day • Back & Biceps",
                    category = "Health",
                    color = "#FF4FD8",
                    eventDate = today.toString(),
                    eventTime = "12:30 PM",
                    isReminderEnabled = true
                )
            )

            repository.insert(
                CalendarEvent(
                    title = "Project Aurora",
                    description = "Review new components",
                    category = "Work",
                    color = "#55EFC4",
                    eventDate = today.toString(),
                    eventTime = "03:00 PM",
                    isReminderEnabled = true
                )
            )

            repository.insert(
                CalendarEvent(
                    title = "Read Book",
                    description = "Atomic Habits • Chapter 5-6",
                    category = "Personal",
                    color = "#FDCB6E",
                    eventDate = today.toString(),
                    eventTime = "07:30 PM",
                    isReminderEnabled = true
                )
            )

            repository.insert(
                CalendarEvent(
                    title = "Team Meeting",
                    description = "Discuss project progress",
                    category = "Work",
                    color = "#55EFC4",
                    eventDate = today.plusDays(1).toString(),
                    eventTime = "10:00 AM",
                    isReminderEnabled = true
                )
            )

            repository.insert(
                CalendarEvent(
                    title = "Client Call",
                    description = "UI/UX feedback session",
                    category = "Work",
                    color = "#55EFC4",
                    eventDate = today.plusDays(2).toString(),
                    eventTime = "02:00 PM",
                    isReminderEnabled = true
                )
            )
        }
    }
}