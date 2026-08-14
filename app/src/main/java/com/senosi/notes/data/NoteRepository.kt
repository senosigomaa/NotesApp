package com.senosi.notes.data

class NoteRepository(
    private val dao: NoteDao
) {

    fun observeNotes() = dao.observeNotes()

    fun observeFavorites() = dao.observeFavorites()

    fun observeTrash() = dao.observeTrash()

    fun observeNote(id: Long) = dao.observeNote(id)

    suspend fun insert(note: Note) {
        dao.insert(note)
    }
//sss
    suspend fun update(note: Note) {
        dao.update(note)
    }

    suspend fun delete(note: Note) {
        dao.delete(note)
    }
}