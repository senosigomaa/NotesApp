package com.senosi.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

private enum class NoteFilter {
    ALL,
    FAVORITES,
    WORK,
    PERSONAL
}

private enum class BottomItem {
    NOTES,
    CALENDAR,
    STATS,
    SETTINGS
}

@Composable
fun HomeScreen(
    notes: List<Note>,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onFavoriteClick: (Note) -> Unit,
    onCalendarClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(NoteFilter.ALL)
    }

    val filteredNotes = notes.filter { note ->

        if (note.isDeleted) {
            return@filter false
        }

        val query = searchText.trim()

        val matchesSearch =
            query.isEmpty() ||
                    note.title.contains(
                        query,
                        ignoreCase = true
                    ) ||
                    note.content.contains(
                        query,
                        ignoreCase = true
                    )

        val noteText =
            "${note.title} ${note.content}"

        val matchesFilter = when (selectedFilter) {

            NoteFilter.ALL -> true

            NoteFilter.FAVORITES ->
                note.isFavorite

            NoteFilter.WORK ->
                noteText.contains(
                    "work",
                    ignoreCase = true
                ) ||
                        noteText.contains(
                            "project",
                            ignoreCase = true
                        ) ||
                        noteText.contains(
                            "study",
                            ignoreCase = true
                        )

            NoteFilter.PERSONAL ->
                noteText.contains(
                    "personal",
                    ignoreCase = true
                ) ||
                        noteText.contains(
                            "journal",
                            ignoreCase = true
                        ) ||
                        noteText.contains(
                            "daily",
                            ignoreCase = true
                        )
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        containerColor = Background,

        bottomBar = {
            NotesBottomNavigation(
                selectedItem = BottomItem.NOTES,
                onNotesClick = {},
                onCalendarClick = onCalendarClick,
                onStatsClick = onStatsClick,
                onSettingsClick = onSettingsClick
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 68.dp)
                    .size(64.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        ambientColor = Primary.copy(alpha = 0.5f),
                        spotColor = Primary.copy(alpha = 0.65f)
                    ),
                shape = CircleShape,
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note",
                    modifier = Modifier.size(31.dp)
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            /*
             * Subtle ambient glow.
             * This is intentionally very low opacity so
             * it does not interfere with readability.
             */
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.10f),
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

                /*
                 * =====================================================
                 * HEADER
                 * =====================================================
                 */

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    HomeCircleButton(
                        onClick = onMenuClick,
                        icon = Icons.Default.Menu
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "My Notes",
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
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HomeCircleButton(
                        onClick = onSearchClick,
                        icon = Icons.Default.Search,
                        tint = Primary
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    HomeCircleButton(
                        onClick = {},
                        icon = Icons.Default.Tune,
                        tint = TextSecondary
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                /*
                 * =====================================================
                 * SEARCH
                 * =====================================================
                 */

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    placeholder = {
                        Text(
                            text = "Search your notes...",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor =
                            Primary.copy(alpha = 0.75f),
                        unfocusedBorderColor =
                            SurfaceLight.copy(alpha = 0.8f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = Primary
                    )
                )

                Spacer(
                    modifier = Modifier.height(17.dp)
                )

                /*
                 * =====================================================
                 * FILTER CHIPS
                 * =====================================================
                 */

                HomeFilterRow(
                    selectedFilter = selectedFilter,
                    onFilterSelected = {
                        selectedFilter = it
                    }
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                /*
                 * =====================================================
                 * SECTION TITLE
                 * =====================================================
                 */

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Recent Notes",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${filteredNotes.size} notes",
                        color = Primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Your latest thoughts",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                /*
                 * =====================================================
                 * NOTES
                 * =====================================================
                 */

                if (filteredNotes.isEmpty()) {

                    EmptyNotesState(
                        onAddNoteClick = onAddNoteClick
                    )

                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement =
                            Arrangement.spacedBy(13.dp),
                        contentPadding = PaddingValues(
                            bottom = 120.dp
                        )
                    ) {

                        items(
                            items = filteredNotes,
                            key = { it.id }
                        ) { note ->

                            HomeNoteCard(
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
}

/*
 * =====================================================================
 * HEADER BUTTON
 * =====================================================================
 */

@Composable
private fun HomeCircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color = TextPrimary
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
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(23.dp)
        )
    }
}

/*
 * =====================================================================
 * FILTER ROW
 * =====================================================================
 */

@Composable
private fun HomeFilterRow(
    selectedFilter: NoteFilter,
    onFilterSelected: (NoteFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(
                rememberScrollState()
            ),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {

        HomeFilterChip(
            text = "All Notes",
            selected =
                selectedFilter == NoteFilter.ALL,
            onClick = {
                onFilterSelected(NoteFilter.ALL)
            }
        )

        HomeFilterChip(
            text = "☆  Favorites",
            selected =
                selectedFilter == NoteFilter.FAVORITES,
            onClick = {
                onFilterSelected(NoteFilter.FAVORITES)
            }
        )

        HomeFilterChip(
            text = "▣  Work",
            selected =
                selectedFilter == NoteFilter.WORK,
            onClick = {
                onFilterSelected(NoteFilter.WORK)
            }
        )

        HomeFilterChip(
            text = "♙  Personal",
            selected =
                selectedFilter == NoteFilter.PERSONAL,
            onClick = {
                onFilterSelected(NoteFilter.PERSONAL)
            }
        )
    }
}

@Composable
private fun HomeFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(25.dp)
            )
            .background(
                if (selected) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Primary,
                            Primary.copy(alpha = 0.72f)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Surface,
                            SurfaceLight.copy(
                                alpha = 0.38f
                            )
                        )
                    )
                }
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    ) {

        Text(
            text = text,
            color = if (selected) {
                Color.White
            } else {
                TextSecondary
            },
            fontSize = 13.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}

/*
 * =====================================================================
 * NOTE CARD
 * =====================================================================
 */

@Composable
private fun HomeNoteCard(
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
        }.getOrDefault(Primary)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Surface,
                            SurfaceLight.copy(
                                alpha = 0.32f
                            )
                        )
                    )
                )
        ) {

            /*
             * Neon side line
             */

            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(174.dp)
                    .background(noteColor)
            )

            /*
             * Main card content
             */

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 15.dp,
                        top = 16.dp,
                        bottom = 15.dp
                    )
            ) {

                Row(
                    verticalAlignment =
                        Alignment.Top
                ) {

                    /*
                     * Icon bubble
                     */

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(
                                RoundedCornerShape(
                                    19.dp
                                )
                            )
                            .background(
                                noteColor.copy(
                                    alpha = 0.10f
                                )
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Edit,
                            contentDescription =
                                null,
                            tint = noteColor,
                            modifier =
                                Modifier.size(29.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(13.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = note.title.ifBlank {
                                "Untitled Note"
                            },
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow =
                                TextOverflow.Ellipsis
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text = note.content.ifBlank {
                                "No content yet"
                            },
                            color = TextSecondary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            maxLines = 2,
                            overflow =
                                TextOverflow.Ellipsis
                        )
                    }

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
                            contentDescription =
                                "Favorite",
                            tint =
                                if (note.isFavorite) {
                                    Primary
                                } else {
                                    TextSecondary
                                },
                            modifier =
                                Modifier.size(27.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                /*
                 * Bottom metadata
                 */

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "9:30 PM",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(
                                Primary
                            )
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Today",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.MoreHoriz,
                        contentDescription =
                            "More",
                        tint = TextMuted,
                        modifier =
                            Modifier.size(23.dp)
                    )
                }
            }
        }
    }
}

/*
 * =====================================================================
 * EMPTY STATE
 * =====================================================================
 */

@Composable
private fun EmptyNotesState(
    onAddNoteClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(
                        RoundedCornerShape(26.dp)
                    )
                    .background(
                        Primary.copy(
                            alpha = 0.10f
                        )
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Edit,
                    contentDescription =
                        null,
                    tint = Primary,
                    modifier =
                        Modifier.size(36.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(17.dp)
            )

            Text(
                text = "Your notes are waiting",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Create your first note",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "+  Create Note",
                color = Primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(
                    onClick = onAddNoteClick
                )
            )
        }
    }
}

/*
 * =====================================================================
 * BOTTOM NAVIGATION
 * =====================================================================
 */

@Composable
private fun NotesBottomNavigation(
    selectedItem: BottomItem,
    onNotesClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 15.dp,
                end = 15.dp,
                bottom = 8.dp
            )
            .clip(
                RoundedCornerShape(25.dp)
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceLight.copy(
                            alpha = 0.82f
                        ),
                        Surface.copy(
                            alpha = 0.98f
                        )
                    )
                )
            )
            .padding(
                horizontal = 5.dp,
                vertical = 8.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            BottomNavItem(
                selected =
                    selectedItem ==
                            BottomItem.NOTES,
                icon =
                    Icons.Default.Edit,
                label = "Notes",
                onClick = onNotesClick,
                modifier =
                    Modifier.weight(1f)
            )

            BottomNavItem(
                selected =
                    selectedItem ==
                            BottomItem.CALENDAR,
                icon =
                    Icons.Default.CalendarMonth,
                label = "Calendar",
                onClick = onCalendarClick,
                modifier =
                    Modifier.weight(1f)
            )

            BottomNavItem(
                selected =
                    selectedItem ==
                            BottomItem.STATS,
                icon =
                    Icons.Default.BarChart,
                label = "Stats",
                onClick = onStatsClick,
                modifier =
                    Modifier.weight(1f)
            )

            BottomNavItem(
                selected =
                    selectedItem ==
                            BottomItem.SETTINGS,
                icon =
                    Icons.Default.Settings,
                label = "Settings",
                onClick = onSettingsClick,
                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(19.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                vertical = 7.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) {
                Primary
            } else {
                TextSecondary
            },
            modifier = Modifier.size(23.dp)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            color = if (selected) {
                Primary
            } else {
                TextSecondary
            },
            fontSize = 10.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}