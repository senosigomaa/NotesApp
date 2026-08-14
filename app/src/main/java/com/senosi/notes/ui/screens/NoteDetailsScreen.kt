package com.senosi.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.AppBackground
import com.senosi.notes.ui.theme.PrimaryPurple
import com.senosi.notes.ui.theme.TextSecondary

@Composable
fun NoteDetailsScreen(
    note: Note,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val noteColor = try {
        Color(android.graphics.Color.parseColor(note.color))
    } catch (_: Exception) {
        PrimaryPurple
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // ─────────────────────────────
        // Header
        // ─────────────────────────────

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF292A2F)
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    imageVector =
                        if (note.isFavorite) {
                            Icons.Default.Star
                        } else {
                            Icons.Outlined.StarBorder
                        },
                    contentDescription = "Favorite",
                    tint =
                        if (note.isFavorite) {
                            Color(0xFFF5B93D)
                        } else {
                            Color(0xFF777780)
                        }
                )
            }

            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = PrimaryPurple
                )
            }
        }

        // ─────────────────────────────
        // Content
        // ─────────────────────────────

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // Small color indicator
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(noteColor)
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // Title
            Text(
                text = note.title,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF25262A)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // Metadata
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Updated recently",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = "•",
                    color = Color(0xFFB5B5BE),
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = "Note",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // Note body
            Text(
                text = note.content,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                lineHeight = 31.sp,
                color = Color(0xFF38383F)
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Color(0xFFE8E7ED)
                    )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            horizontal = 3.dp
                        )
                    )

                    Text(
                        text = "Edit Note",
                        fontSize = 15.sp
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(14.dp)
                        )
                        .background(
                            Color(0xFFFFEEEE)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color(0xFFE05252)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}