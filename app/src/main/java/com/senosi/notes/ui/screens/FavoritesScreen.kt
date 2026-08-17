package com.senosi.notes.ui.screens
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary

@Composable
fun FavoritesScreen(
    notes: List<Note>,
    onBackClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onFavoriteClick: (Note) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // ============================================================
        // AMBIENT GLOW
        // ============================================================

        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Primary.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // ========================================================
            // HEADER
            // ========================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                FavoritesCircleButton(
                    onClick = onBackClick,
                    icon = Icons.Default.ArrowBack
                )

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Favorites",
                            color = TextPrimary,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.width(7.dp)
                        )

                        Text(
                            text = "✦",
                            color = Primary,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Your favorite notes",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Primary.copy(alpha = 0.10f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // ========================================================
            // SUMMARY CARD
            // ========================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(22.dp)
                    )
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                SurfaceLight.copy(alpha = 0.72f),
                                Surface
                            )
                        )
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 17.dp
                    )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .background(
                                Primary.copy(alpha = 0.12f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(13.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "${notes.size} Favorite Notes",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Notes you marked as important",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "★",
                        color = Primary,
                        fontSize = 25.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // ========================================================
            // SECTION TITLE
            // ========================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Saved Favorites",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${notes.size}",
                    color = Primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Quick access to notes you love",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // ========================================================
            // EMPTY STATE
            // ========================================================

            if (notes.isEmpty()) {

                FavoritesEmptyState()

            } else {

                // ====================================================
                // FAVORITES LIST
                // ====================================================

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    verticalArrangement =
                        Arrangement.spacedBy(13.dp),

                    contentPadding =
                        PaddingValues(
                            bottom = 24.dp
                        )
                ) {

                    items(
                        items = notes,
                        key = {
                            it.id
                        }
                    ) { note ->

                        FavoriteNoteCard(
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
}

// ============================================================================
// HEADER BUTTON
// ============================================================================

@Composable
private fun FavoritesCircleButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    IconButton(
        onClick = onClick,

        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        SurfaceLight.copy(alpha = 0.85f),
                        Surface.copy(alpha = 0.95f)
                    )
                )
            )
    ) {

        Icon(
            imageVector = icon,
            contentDescription = "Back",
            tint = TextPrimary,
            modifier = Modifier.size(23.dp)
        )
    }
}

// ============================================================================
// FAVORITE NOTE CARD
// ============================================================================

@Composable
private fun FavoriteNoteCard(
    note: Note,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val noteColor = remember(note.color) {

        runCatching {

            Color(
                android.graphics.Color.parseColor(
                    note.color
                )
            )

        }.getOrDefault(
            Primary
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(alpha = 0.32f)
                    )
                )
            )
            .clickable(
                onClick = onClick
            )
    ) {

        // ==========================================================
        // COLOR LINE
        // ==========================================================

        Box(
            modifier = Modifier
                .width(5.dp)
                .height(158.dp)
                .background(noteColor)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 15.dp,
                    top = 15.dp,
                    bottom = 14.dp,
                    end = 10.dp
                )
        ) {

            Row(
                verticalAlignment = Alignment.Top
            ) {

                // ==================================================
                // NOTE ICON
                // ==================================================

                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            noteColor.copy(alpha = 0.10f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = noteColor,
                        modifier = Modifier.size(27.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                // ==================================================
                // TITLE + CONTENT
                // ==================================================

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = note.title.ifBlank {
                            "Untitled Note"
                        },
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = note.content.ifBlank {
                            "No content yet"
                        },
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // ==================================================
                // FAVORITE BUTTON
                // ==================================================

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(42.dp)
                ) {

                    Icon(
                        imageVector =
                            if (note.isFavorite) {
                                Icons.Default.Star
                            } else {
                                Icons.Outlined.StarBorder
                            },

                        contentDescription = "Remove from favorites",

                        tint =
                            if (note.isFavorite) {
                                Primary
                            } else {
                                TextSecondary
                            },

                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            // ========================================================
            // META
            // ========================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Favorite",
                    color = Primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Primary)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Saved note",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Primary.copy(alpha = 0.75f),
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

// ============================================================================
// EMPTY STATE
// ============================================================================

@Composable
private fun FavoritesEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = 50.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(
                        RoundedCornerShape(27.dp)
                    )
                    .background(
                        Primary.copy(alpha = 0.10f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "No favorites yet",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = "Star your important notes",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "and they will appear here.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}