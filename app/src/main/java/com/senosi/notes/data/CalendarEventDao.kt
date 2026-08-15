package com.senosi.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEventDao {

    @Query(
        """
        SELECT * FROM calendar_events
        WHERE eventDate = :date
        ORDER BY updatedAt ASC
        """
    )
    fun observeEventsForDate(
        date: String
    ): Flow<List<CalendarEvent>>

    @Query(
        """
        SELECT * FROM calendar_events
        WHERE eventDate >= :fromDate
        ORDER BY eventDate ASC, updatedAt ASC
        """
    )
    fun observeUpcomingEvents(
        fromDate: String
    ): Flow<List<CalendarEvent>>

    @Query(
        """
        SELECT * FROM calendar_events
        WHERE eventDate = :date
        LIMIT 1
        """
    )
    fun observeFirstEventForDate(
        date: String
    ): Flow<CalendarEvent?>

    @Insert
    suspend fun insert(
        event: CalendarEvent
    )

    @Update
    suspend fun update(
        event: CalendarEvent
    )

    @Delete
    suspend fun delete(
        event: CalendarEvent
    )
}