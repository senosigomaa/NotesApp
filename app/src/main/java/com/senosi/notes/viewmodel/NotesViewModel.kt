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

    init {
        val database = NotesDatabase.getInstance(application)
        repository = NoteRepository(database.noteDao())

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
                    updatedAt = System.currentTimeMillis()
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

    fun moveToTrash(note: Note) {
        updateNote(
            note.copy(
                isDeleted = true,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    fun restore(note: Note) {
        updateNote(
            note.copy(
                isDeleted = false,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    private fun seedNotes() {
        viewModelScope.launch {

            val existingNotes = repository
                .observeNotes()
                .collect { notes ->

                    if (notes.isEmpty()) {

                        repository.insert(
                            Note(
                                title = "Shopping List",
                                content = "Milk, Bread, Eggs, Fruits...",
                                color = "#6C5CE7"
                            )
                        )

                        repository.insert(
                            Note(
                                title = "Study Plan",
                                content = "Math\nPhysics\nChemistry\nEnglish",
                                color = "#FDCB6E"
                            )
                        )

                        repository.insert(
                            Note(
                                title = "Idea for Project",
                                content = "A new app idea for students...",
                                color = "#55EFC4"
                            )
                        )

                        repository.insert(
                            Note(
                                title = "Daily Thoughts",
                                content = "Today was a productive day!",
                                color = "#FF7675"
                            )
                        )
                    }

                    return@collect
                }
        }
    }
}