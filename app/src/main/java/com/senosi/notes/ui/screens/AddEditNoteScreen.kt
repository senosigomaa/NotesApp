package com.senosi.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun AddEditNoteScreen(
    note: Note?,
    onBackClick: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember(note?.id) {
        mutableStateOf(note?.title ?: "")
    }

    var content by remember(note?.id) {
        mutableStateOf(note?.content ?: "")
    }

    var selectedColor by remember(note?.id) {
        mutableStateOf(note?.color ?: "#6C5CE7")
    }

    val colors = listOf(
        "#6C5CE7",
        "#A29BFE",
        "#55EFC4",
        "#FDCB6E",
        "#FF7675",
        "#DDE1E6"
    )

    Scaffold(
        containerColor = AppBackground,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (note == null) "Add Note" else "Edit Note",
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Text(
                            text = "←",
                            fontSize = 28.sp,
                            color = PrimaryPurple
                        )
                    }
                },

                actions = {
                    IconButton(
                        onClick = {
                            if (
                                title.isNotBlank() &&
                                content.isNotBlank()
                            ) {
                                onSave(
                                    title.trim(),
                                    content.trim(),
                                    selectedColor
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 25.sp,
                            color = PrimaryPurple
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp),

            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Title")
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                placeholder = {
                    Text(
                        text = "Write your note...",
                        color = TextSecondary
                    )
                },

                shape = RoundedCornerShape(14.dp)
            )

            Text(
                text = "Color",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                colors.forEach { color ->

                    val composeColor = Color(
                        android.graphics.Color.parseColor(color)
                    )

                    val selected = color == selectedColor

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                color = if (selected) {
                                    PrimaryPurple.copy(alpha = 0.15f)
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .padding(4.dp)
                            .background(
                                color = composeColor,
                                shape = CircleShape
                            )
                            .clickable {
                                selectedColor = color
                            }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Button(
                onClick = {
                    if (
                        title.isNotBlank() &&
                        content.isNotBlank()
                    ) {
                        onSave(
                            title.trim(),
                            content.trim(),
                            selectedColor
                        )
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Text(
                    text = if (note == null) {
                        "Save Note"
                    } else {
                        "Save Changes"
                    },

                    fontSize = 16.sp
                )
            }
        }
    }
}