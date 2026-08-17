package com.senosi.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val content: String,

    val color: String = "#7C5CFC",

    val isFavorite: Boolean = false,

    val isDeleted: Boolean = false,

    val deletedAt: Long? = null,

    val updatedAt: Long = System.currentTimeMillis()
)