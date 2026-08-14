package com.senosi.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(
        """
        SELECT * FROM notes
        WHERE isDeleted = 0
        ORDER BY updatedAt DESC
        """
    )
    fun observeNotes(): Flow<List<Note>>

    @Query(
        """
        SELECT * FROM notes
        WHERE isDeleted = 0
        AND isFavorite = 1
        ORDER BY updatedAt DESC
        """
    )
    fun observeFavorites(): Flow<List<Note>>

    @Query(
        """
        SELECT * FROM notes
        WHERE isDeleted = 1
        ORDER BY updatedAt DESC
        """
    )
    fun observeTrash(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun observeNote(id: Long): Flow<Note?>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}