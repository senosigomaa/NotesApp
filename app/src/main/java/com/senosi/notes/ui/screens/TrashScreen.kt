package com.senosi.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Danger
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary
import com.senosi.notes.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

private const val TRASH_RETENTION_DAYS = 30L

@Composable
fun TrashScreen(
    onBackClick: () -> Unit = {},
    onNoteClick: (Long) -> Unit = {},
    onPermanentDelete: (Note) -> Unit = {},
    onDeleteAll: (List<Note>) -> Unit = {},
    viewModel: NotesViewModel = viewModel()
) {
    val trashNotes by viewModel
        .observeTrash()
        .collectAsState(initial = emptyList())

    var searchText by remember {
        mutableStateOf("")
    }

    var isSelectionMode by remember {
        mutableStateOf(false)
    }

    var selectedIds by remember {
        mutableStateOf(setOf<Long>())
    }

    val filteredNotes = remember(
        trashNotes,
        searchText
    ) {
        val query = searchText.trim()

        if (query.isEmpty()) {
            trashNotes
        } else {
            trashNotes.filter { note ->
                note.title.contains(
                    query,
                    ignoreCase = true
                ) ||
                        note.content.contains(
                            query,
                            ignoreCase = true
                        )
            }
        }
    }

    val selectedNotes = trashNotes.filter {
        it.id in selectedIds
    }

    val allVisibleSelected =
        filteredNotes.isNotEmpty() &&
                filteredNotes.all {
                    it.id in selectedIds
                }

    val daysLeftForOldestNote = trashNotes
        .minOfOrNull {
            calculateDaysLeft(it.updatedAt)
        }
        ?: TRASH_RETENTION_DAYS

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        /*
         * ============================================================
         * AMBIENT GLOW
         * ============================================================
         */

        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Danger.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 16.dp,
                bottom = 125.dp
            ),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            /*
             * ========================================================
             * HEADER
             * ========================================================
             */

            item {

                TrashHeader(
                    isSelectionMode = isSelectionMode,
                    selectedCount = selectedIds.size,
                    onBackClick = {
                        if (isSelectionMode) {
                            isSelectionMode = false
                            selectedIds = emptySet()
                        } else {
                            onBackClick()
                        }
                    },
                    onSearchChanged = {
                        searchText = it
                    },
                    searchText = searchText
                )
            }

            /*
             * ========================================================
             * SEARCH
             * ========================================================
             */

            item {

                TrashSearchBar(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    }
                )
            }

            /*
             * ========================================================
             * AUTO DELETE CARD
             * ========================================================
             */

            item {

                TrashInfoCard(
                    notesCount = trashNotes.size,
                    daysLeft = daysLeftForOldestNote
                )
            }

            /*
             * ========================================================
             * SELECT / FILTER ROW
             * ========================================================
             */

            item {

                TrashSectionHeader(
                    notesCount = filteredNotes.size,
                    selectionMode = isSelectionMode,
                    allSelected = allVisibleSelected,

                    onSelectionClick = {

                        isSelectionMode = !isSelectionMode

                        if (!isSelectionMode) {
                            selectedIds = emptySet()
                        }
                    },

                    onSelectAllClick = {

                        if (allVisibleSelected) {

                            selectedIds =
                                selectedIds - filteredNotes
                                    .map { it.id }
                                    .toSet()

                        } else {

                            selectedIds =
                                selectedIds + filteredNotes
                                    .map { it.id }
                                    .toSet()
                        }
                    }
                )
            }

            /*
             * ========================================================
             * EMPTY STATE
             * ========================================================
             */

            if (filteredNotes.isEmpty()) {

                item {

                    TrashEmptyState(
                        hasSearch =
                            searchText.isNotBlank(),
                        onClearSearch = {
                            searchText = ""
                        }
                    )
                }

            } else {

                /*
                 * ====================================================
                 * TRASH NOTES
                 * ====================================================
                 */

                items(
                    items = filteredNotes,
                    key = { it.id }
                ) { note ->

                    TrashNoteCard(
                        note = note,
                        isSelected =
                            note.id in selectedIds,
                        selectionMode =
                            isSelectionMode,

                        onClick = {

                            if (isSelectionMode) {

                                selectedIds =
                                    toggleSelection(
                                        selectedIds,
                                        note.id
                                    )

                            } else {

                                onNoteClick(note.id)
                            }
                        },

                        onRestoreClick = {

                            viewModel.restore(note)

                            if (note.id in selectedIds) {

                                selectedIds =
                                    selectedIds - note.id
                            }
                        },

                        onDeleteClick = {

                            onPermanentDelete(note)

                            selectedIds =
                                selectedIds - note.id
                        }
                    )
                }

                /*
                 * ====================================================
                 * BULK ACTIONS
                 * ====================================================
                 */

                item {

                    TrashBulkActions(
                        enabled =
                            selectedNotes.isNotEmpty(),

                        selectedCount =
                            selectedNotes.size,

                        onRestoreAll = {

                            selectedNotes.forEach {
                                viewModel.restore(it)
                            }

                            selectedIds = emptySet()
                            isSelectionMode = false
                        },

                        onDeleteAll = {

                            if (selectedNotes.isNotEmpty()) {

                                onDeleteAll(
                                    selectedNotes
                                )

                                selectedIds = emptySet()
                                isSelectionMode = false
                            }
                        }
                    )
                }
            }

            /*
             * ========================================================
             * FOOTER TIP
             * ========================================================
             */

            item {

                TrashTip()
            }
        }
    }
}

/*
 * =====================================================================
 * HEADER
 * =====================================================================
 */

@Composable
private fun TrashHeader(
    isSelectionMode: Boolean,
    selectedCount: Int,
    onBackClick: () -> Unit,
    onSearchChanged: (String) -> Unit,
    searchText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                bottom = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TrashCircleButton(
            icon = if (isSelectionMode) {
                Icons.Default.Close
            } else {
                Icons.Default.ArrowBack
            },
            contentDescription = "Back",
            onClick = onBackClick
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
                    text = if (isSelectionMode) {
                        "$selectedCount Selected"
                    } else {
                        "Trash"
                    },
                    color = TextPrimary,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                if (!isSelectionMode) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Danger,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = if (isSelectionMode) {
                    "Select notes to restore or delete"
                } else {
                    "Deleted notes are kept for 30 days"
                },
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 2
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        TrashCircleButton(
            icon = Icons.Default.Search,
            contentDescription = "Search",
            onClick = {
                onSearchChanged(searchText)
            },
            tint = Primary
        )

        Spacer(
            modifier = Modifier.width(7.dp)
        )

        TrashCircleButton(
            icon = Icons.Default.Tune,
            contentDescription = "Filter",
            onClick = {},
            tint = TextSecondary
        )
    }
}

/*
 * =====================================================================
 * CIRCLE BUTTON
 * =====================================================================
 */

@Composable
private fun TrashCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = TextPrimary
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        SurfaceLight.copy(alpha = 0.85f),
                        Surface.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight.copy(alpha = 0.65f),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

/*
 * =====================================================================
 * SEARCH
 * =====================================================================
 */

@Composable
private fun TrashSearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        singleLine = true,
        shape = RoundedCornerShape(27.dp),
        placeholder = {
            Text(
                text = "Search deleted notes...",
                color = TextSecondary,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(21.dp)
            )
        },
        colors =
            androidx.compose.material3.OutlinedTextFieldDefaults
                .colors(
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface,
                    focusedBorderColor =
                        Primary.copy(alpha = 0.65f),
                    unfocusedBorderColor =
                        SurfaceLight.copy(alpha = 0.8f),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = Primary
                )
    )
}

/*
 * =====================================================================
 * INFO CARD
 * =====================================================================
 */

@Composable
private fun TrashInfoCard(
    notesCount: Int,
    daysLeft: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(alpha = 0.38f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight.copy(alpha = 0.8f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .background(
                    Danger.copy(alpha = 0.10f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Danger,
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Deleted notes stay for 30 days",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = if (notesCount == 0) {
                    "Your trash is empty."
                } else {
                    "Restore them anytime before they expire."
                },
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 2
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = daysLeft.coerceIn(
                    0L,
                    TRASH_RETENTION_DAYS
                ).toString(),
                color = Danger,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "days left",
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

/*
 * =====================================================================
 * SECTION HEADER
 * =====================================================================
 */

@Composable
private fun TrashSectionHeader(
    notesCount: Int,
    selectionMode: Boolean,
    allSelected: Boolean,
    onSelectionClick: () -> Unit,
    onSelectAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "All Deleted ($notesCount)",
            color = Primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        if (selectionMode) {

            Text(
                text = if (allSelected) {
                    "Clear All"
                } else {
                    "Select All"
                },
                color = Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .clickable(
                        onClick = onSelectAllClick
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 7.dp
                    )
            )

        } else {

            Text(
                text = "Select",
                color = Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .clickable(
                        onClick = onSelectionClick
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 7.dp
                    )
            )
        }
    }
}

/*
 * =====================================================================
 * NOTE CARD
 * =====================================================================
 */

@Composable
private fun TrashNoteCard(
    note: Note,
    isSelected: Boolean,
    selectionMode: Boolean,
    onClick: () -> Unit,
    onRestoreClick: () -> Unit,
    onDeleteClick: () -> Unit
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

    val daysLeft =
        calculateDaysLeft(note.updatedAt)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(alpha = 0.30f)
                    )
                )
            )
            .border(
                width = if (isSelected) {
                    1.5.dp
                } else {
                    1.dp
                },
                color = if (isSelected) {
                    Primary.copy(alpha = 0.75f)
                } else {
                    SurfaceLight.copy(alpha = 0.75f)
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
    ) {

        /*
         * ============================================================
         * COLOR LINE
         * ============================================================
         */

        Box(
            modifier = Modifier
                .width(4.dp)
                .height(128.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            noteColor,
                            noteColor.copy(alpha = 0.35f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 13.dp,
                    top = 13.dp,
                    bottom = 12.dp,
                    end = 9.dp
                )
        ) {

            Row(
                verticalAlignment = Alignment.Top
            ) {

                /*
                 * ====================================================
                 * NOTE ICON
                 * ====================================================
                 */

                Box(
                    modifier = Modifier
                        .size(53.dp)
                        .clip(
                            RoundedCornerShape(17.dp)
                        )
                        .background(
                            noteColor.copy(alpha = 0.10f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Delete,
                        contentDescription = null,
                        tint = noteColor,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                /*
                 * ====================================================
                 * CONTENT
                 * ====================================================
                 */

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = note.title.ifBlank {
                            "Untitled Note"
                        },
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = note.content.ifBlank {
                            "No content"
                        },
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                /*
                 * ====================================================
                 * SELECT CHECK
                 * ====================================================
                 */

                if (selectionMode) {

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) {
                                    Primary
                                } else {
                                    SurfaceLight
                                }
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) {
                                    Primary
                                } else {
                                    TextMuted
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        if (isSelected) {

                            Icon(
                                imageVector =
                                    Icons.Default.Check,
                                contentDescription =
                                    "Selected",
                                tint = Color.White,
                                modifier =
                                    Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(11.dp)
            )

            /*
             * ========================================================
             * BOTTOM ROW
             * ========================================================
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Deleted",
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = formatDate(note.updatedAt),
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Primary)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = formatTime(note.updatedAt),
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                /*
                 * ====================================================
                 * DAYS LEFT
                 * ====================================================
                 */

                Text(
                    text = "$daysLeft days left",
                    color = if (daysLeft <= 5) {
                        Danger
                    } else {
                        Primary
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            /*
             * ========================================================
             * ACTIONS
             * ========================================================
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                TrashActionButton(
                    text = "Restore",
                    icon = Icons.Default.Restore,
                    tint = Primary,
                    modifier = Modifier.weight(1f),
                    onClick = onRestoreClick
                )

                TrashActionButton(
                    text = "Delete",
                    icon = Icons.Default.Delete,
                    tint = Danger,
                    modifier = Modifier.weight(1f),
                    onClick = onDeleteClick
                )
            }
        }
    }
}

/*
 * =====================================================================
 * ACTION BUTTON
 * =====================================================================
 */

@Composable
private fun TrashActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                tint.copy(alpha = 0.08f)
            )
            .border(
                width = 1.dp,
                color = tint.copy(alpha = 0.16f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )

        Spacer(
            modifier = Modifier.width(5.dp)
        )

        Text(
            text = text,
            color = tint,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/*
 * =====================================================================
 * BULK ACTIONS
 * =====================================================================
 */

@Composable
private fun TrashBulkActions(
    enabled: Boolean,
    selectedCount: Int,
    onRestoreAll: () -> Unit,
    onDeleteAll: () -> Unit
) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = SurfaceLight,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {

            TrashBulkButton(
                text = if (enabled) {
                    "Restore $selectedCount"
                } else {
                    "Restore All"
                },
                icon = Icons.Default.Restore,
                tint = Primary,
                enabled = enabled,
                modifier = Modifier.weight(1f),
                onClick = onRestoreAll
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(62.dp)
                    .background(SurfaceLight)
            )

            TrashBulkButton(
                text = if (enabled) {
                    "Delete $selectedCount"
                } else {
                    "Delete All"
                },
                icon = Icons.Default.Delete,
                tint = Danger,
                enabled = enabled,
                modifier = Modifier.weight(1f),
                onClick = onDeleteAll
            )
        }
    }
}

/*
 * =====================================================================
 * BULK BUTTON
 * =====================================================================
 */

@Composable
private fun TrashBulkButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    enabled: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(62.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(35.dp)
                .clip(
                    RoundedCornerShape(11.dp)
                )
                .background(
                    tint.copy(
                        alpha = if (enabled) {
                            0.10f
                        } else {
                            0.04f
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint.copy(
                    alpha = if (enabled) {
                        1f
                    } else {
                        0.35f
                    }
                ),
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(7.dp)
        )

        Text(
            text = text,
            color = tint.copy(
                alpha = if (enabled) {
                    1f
                } else {
                    0.35f
                }
            ),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/*
 * =====================================================================
 * EMPTY STATE
 * =====================================================================
 */

@Composable
private fun TrashEmptyState(
    hasSearch: Boolean,
    onClearSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 40.dp,
                bottom = 30.dp
            ),
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
                        Primary.copy(alpha = 0.09f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = if (hasSearch) {
                    "No matching notes"
                } else {
                    "Your trash is empty"
                },
                color = TextPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = if (hasSearch) {
                    "Try another search"
                } else {
                    "Deleted notes will appear here"
                },
                color = TextSecondary,
                fontSize = 12.sp
            )

            if (hasSearch) {

                Spacer(
                    modifier = Modifier.height(13.dp)
                )

                Text(
                    text = "Clear Search",
                    color = Primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(
                        onClick = onClearSearch
                    )
                )
            }
        }
    }
}

/*
 * =====================================================================
 * TIP
 * =====================================================================
 */

@Composable
private fun TrashTip() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                Primary.copy(alpha = 0.06f)
            )
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.18f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    Primary.copy(alpha = 0.10f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "✦",
                color = Primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column {

            Text(
                text = "Tip",
                color = Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = "You can restore notes anytime before they are permanently deleted.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

/*
 * =====================================================================
 * HELPERS
 * =====================================================================
 */

private fun toggleSelection(
    selectedIds: Set<Long>,
    id: Long
): Set<Long> {
    return if (id in selectedIds) {
        selectedIds - id
    } else {
        selectedIds + id
    }
}

private fun calculateDaysLeft(
    updatedAt: Long
): Long {

    val elapsedMillis =
        System.currentTimeMillis() - updatedAt

    val elapsedDays =
        ceil(
            elapsedMillis.toDouble() /
                    (24L * 60L * 60L * 1000L)
        ).toLong()

    return (
            TRASH_RETENTION_DAYS -
                    elapsedDays
            )
        .coerceIn(
            0L,
            TRASH_RETENTION_DAYS
        )
}

private fun formatDate(
    timestamp: Long
): String {

    return runCatching {

        SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.US
        ).format(
            Date(timestamp)
        )

    }.getOrDefault("-")
}

private fun formatTime(
    timestamp: Long
): String {

    return runCatching {

        SimpleDateFormat(
            "h:mm a",
            Locale.US
        ).format(
            Date(timestamp)
        )

    }.getOrDefault("-")
}