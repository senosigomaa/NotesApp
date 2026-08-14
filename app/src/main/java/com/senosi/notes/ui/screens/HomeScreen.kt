package com.senosi.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.AppBackground
import com.senosi.notes.ui.theme.PrimaryPurple
import com.senosi.notes.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    notes: List<Note>,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onFavoriteClick: (Note) -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }

    val filteredNotes = notes.filter {
        it.title.contains(searchText, ignoreCase = true) ||
                it.content.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        containerColor = AppBackground,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Notes",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436)
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Text(
                            text = "☰",
                            fontSize = 25.sp,
                            color = PrimaryPurple
                        )
                    }
                },

                actions = {
                    IconButton(
                        onClick = onSearchClick
                    ) {
                        Text(
                            text = "⌕",
                            fontSize = 30.sp,
                            color = PrimaryPurple
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = PrimaryPurple,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 28.sp
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search notes...",
                        color = TextSecondary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredNotes,
                    key = { it.id }
                ) { note ->

                    NoteCard(
                        note = note,
                        onClick = {
                            onNoteClick(note.id)
                        },
                        onFavoriteClick = {
                            onFavoriteClick(note)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val noteColor = try {
        Color(
            android.graphics.Color.parseColor(note.color)
        )
    } catch (_: Exception) {
        PrimaryPurple
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // اللون الجانبي للنوت
            Spacer(
                modifier = Modifier
                    .size(
                        width = 5.dp,
                        height = 88.dp
                    )
                    .background(noteColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 14.dp,
                        vertical = 13.dp
                    )
            ) {

                Text(
                    text = note.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3436),
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    maxLines = 2
                )
            }

            IconButton(
                onClick = onFavoriteClick
            ) {
                Text(
                    text = if (note.isFavorite) "★" else "☆",
                    fontSize = 24.sp,
                    color = if (note.isFavorite) {
                        Color(0xFFF5B93D)
                    } else {
                        Color(0xFFB8B8C0)
                    }
                )
            }
        }
    }
}