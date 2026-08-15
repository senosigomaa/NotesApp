package com.senosi.notes.data

class CalendarEventRepository(
    private val dao: CalendarEventDao
) {

    fun observeEventsForDate(
        date: String
    ) = dao.observeEventsForDate(date)

    fun observeUpcomingEvents(
        fromDate: String
    ) = dao.observeUpcomingEvents(fromDate)

    fun observeFirstEventForDate(
        date: String
    ) = dao.observeFirstEventForDate(date)

    suspend fun insert(
        event: CalendarEvent
    ) {
        dao.insert(event)
    }

    suspend fun update(
        event: CalendarEvent
    ) {
        dao.update(event)
    }

    suspend fun delete(
        event: CalendarEvent
    ) {
        dao.delete(event)
    }
}