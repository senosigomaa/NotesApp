package com.senosi.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senosi.notes.data.Note
import com.senosi.notes.data.NoteRepository
import com.senosi.notes.data.NotesDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: NoteRepository

    companion object {
        private const val TRASH_RETENTION_DAYS = 30L

        private const val MILLIS_PER_DAY =
            24L * 60L * 60L * 1000L
    }

    init {

        val database =
            NotesDatabase.getInstance(application)

        repository =
            NoteRepository(
                database.noteDao()
            )

        cleanupExpiredTrash()

        seedNotes()
    }

    fun observeNotes(): Flow<List<Note>> {
        return repository.observeNotes()
    }

    fun observeFavorites(): Flow<List<Note>> {
        return repository.observeFavorites()
    }

    fun observeTrash(): Flow<List<Note>> {
        return repository.observeTrash()
    }

    fun observeNote(id: Long): Flow<Note?> {
        return repository.observeNote(id)
    }

    fun addNote(
        title: String,
        content: String,
        color: String
    ) {
        viewModelScope.launch {

            repository.insert(
                Note(
                    title = title,
                    content = content,
                    color = color
                )
            )
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {

            repository.update(
                note.copy(
                    updatedAt =
                        System.currentTimeMillis()
                )
            )
        }
    }

    fun toggleFavorite(note: Note) {
        updateNote(
            note.copy(
                isFavorite = !note.isFavorite
            )
        )
    }

    /**
     * Move note to Trash.
     *
     * deletedAt is set only when the note
     * enters Trash.
     */
    fun moveToTrash(note: Note) {

        viewModelScope.launch {

            val now =
                System.currentTimeMillis()

            repository.update(
                note.copy(
                    isDeleted = true,
                    deletedAt = now,
                    updatedAt = now
                )
            )
        }
    }

    /**
     * Restore a note from Trash.
     */
    fun restore(note: Note) {

        viewModelScope.launch {

            val now =
                System.currentTimeMillis()

            repository.update(
                note.copy(
                    isDeleted = false,
                    deletedAt = null,
                    updatedAt = now
                )
            )
        }
    }

    /**
     * Permanently delete one note.
     */
    fun permanentlyDelete(note: Note) {

        viewModelScope.launch {
            repository.delete(note)
        }
    }

    /**
     * Restore every note currently in Trash.
     */
    fun restoreAllTrash() {

        viewModelScope.launch {

            repository.restoreAllTrash(
                updatedAt =
                    System.currentTimeMillis()
            )
        }
    }

    /**
     * Permanently delete every note in Trash.
     */
    fun deleteAllTrash() {

        viewModelScope.launch {
            repository.deleteAllTrash()
        }
    }

    /**
     * Remove notes that stayed in Trash
     * for 30 days or more.
     *
     * This runs when the app starts.
     */
    private fun cleanupExpiredTrash() {

        viewModelScope.launch {

            val expirationTime =
                System.currentTimeMillis() -
                        (
                                TRASH_RETENTION_DAYS *
                                        MILLIS_PER_DAY
                                )

            repository.deleteExpiredTrash(
                expirationTime
            )
        }
    }

    private fun seedNotes() {

        viewModelScope.launch {

            repository
                .observeNotes()
                .collect { notes ->

                    if (notes.isEmpty()) {

                        repository.insert(
                            Note(
                                title = "Shopping List",
                                content =
                                    "Milk, Bread, Eggs, Fruits...",
                                color = "#6C5CE7"
                            )
                        )

                        repository.insert(
                            Note(
                                title = "Study Plan",
                                content =
                                    "Math\nPhysics\nChemistry\nEnglish",
                                color = "#FDCB6E"
                            )
                        )

                        repository.insert(
                            Note(
                                title = "Idea for Project",
                                content =
                                    "A new app idea for students...",
                                color = "#55EFC4"
                            )
                        )

                        repository.insert(
                            Note(
                                title = "Daily Thoughts",
                                content =
                                    "Today was a productive day!",
                                color = "#FF7675"
                            )
                        )
                    }
                }
        }
    }
}