package com.senosi.notes.ui.screens
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Danger
import com.senosi.notes.ui.theme.NoteGray
import com.senosi.notes.ui.theme.NoteLavender
import com.senosi.notes.ui.theme.NoteMint
import com.senosi.notes.ui.theme.NotePurple
import com.senosi.notes.ui.theme.NoteYellow
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary

@Composable
fun AddEditNoteScreen(
    note: Note?,
    onBackClick: () -> Unit,
    onSave: (String, String, String) -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    var title by rememberSaveable(note?.id) {
        mutableStateOf(note?.title ?: "")
    }

    var content by rememberSaveable(note?.id) {
        mutableStateOf(note?.content ?: "")
    }

    var selectedColor by rememberSaveable(note?.id) {
        mutableStateOf(note?.color ?: "#7C5CFC")
    }

    val scrollState = rememberScrollState()

    val isEditing = note != null

    val selectedColorValue = parseNoteColor(
        selectedColor = selectedColor,
        fallback = Primary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 18.dp, bottom = 28.dp)
    ) {

        // ------------------------------------------------------------
        // HEADER
        // ------------------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Surface)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = if (isEditing) {
                        "Edit Note"
                    } else {
                        "New Note"
                    },
                    color = TextPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = if (isEditing) {
                        "Last updated recently"
                    } else {
                        "Create something new"
                    },
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Primary)
                    .clickable {
                        onSave(
                            title.trim(),
                            content.trim(),
                            selectedColor
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ------------------------------------------------------------
        // TITLE
        // ------------------------------------------------------------

        SectionHeader(
            title = "TITLE",
            icon = "✦",
            counter = "${title.length}/100"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        NeonTextField(
            value = title,
            onValueChange = {
                if (it.length <= 100) {
                    title = it
                }
            },
            placeholder = "Give your note a title...",
            singleLine = true,
            textSize = 22.sp,
            focusedColor = selectedColorValue
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ------------------------------------------------------------
        // CATEGORY
        // ------------------------------------------------------------

        SectionHeader(
            title = "CATEGORY",
            icon = "◇"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        CategorySelector()

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ------------------------------------------------------------
        // CONTENT
        // ------------------------------------------------------------

        SectionHeader(
            title = "CONTENT",
            icon = "▣",
            counter = "${content.length}/4000"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ContentEditor(
            value = content,
            onValueChange = {
                if (it.length <= 4000) {
                    content = it
                }
            },
            focusedColor = selectedColorValue
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ------------------------------------------------------------
        // COLOR
        // ------------------------------------------------------------

        SectionHeader(
            title = "CHOOSE COLOR",
            icon = "✦"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        ColorSelector(
            selectedColor = selectedColor,
            onColorSelected = {
                selectedColor = it
            }
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // ------------------------------------------------------------
        // SAVE
        // ------------------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Primary)
                .clickable {
                    onSave(
                        title.trim(),
                        content.trim(),
                        selectedColor
                    )
                },
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = if (isEditing) {
                        "Save Changes"
                    } else {
                        "Create Note"
                    },
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ------------------------------------------------------------
        // DELETE
        // ------------------------------------------------------------

        if (isEditing) {

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Danger.copy(alpha = 0.10f))
                    .border(
                        width = 1.dp,
                        color = Danger.copy(alpha = 0.28f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        onDeleteClick()
                    },
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = Danger,
                        modifier = Modifier.size(21.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Delete Note",
                        color = Danger,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ============================================================================
// SECTION HEADER
// ============================================================================

@Composable
private fun SectionHeader(
    title: String,
    icon: String,
    counter: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "$icon  $title",
            color = Primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        if (counter != null) {
            Text(
                text = counter,
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}

// ============================================================================
// TITLE FIELD
// ============================================================================

@Composable
private fun NeonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    textSize: TextUnit,
    focusedColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Surface.copy(alpha = 0.82f))
            .border(
                width = 1.dp,
                color = focusedColor.copy(alpha = 0.70f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(
                horizontal = 18.dp,
                vertical = 18.dp
            )
    ) {

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = TextMuted,
                fontSize = textSize
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = TextStyle(
                color = TextPrimary,
                fontSize = textSize,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ============================================================================
// CATEGORY
// ============================================================================

@Composable
private fun CategorySelector() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Surface)
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { },
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Primary.copy(alpha = 0.18f))
                    .border(
                        width = 1.dp,
                        color = Primary.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "◇",
                    color = PrimaryLightSafe(),
                    fontSize = 22.sp
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Text(
                text = "Personal",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "⌄",
                color = TextSecondary,
                fontSize = 25.sp
            )
        }
    }
}

// ============================================================================
// CONTENT EDITOR
// ============================================================================

@Composable
private fun ContentEditor(
    value: String,
    onValueChange: (String) -> Unit,
    focusedColor: Color
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface.copy(alpha = 0.82f))
            .border(
                width = 1.dp,
                color = focusedColor.copy(alpha = 0.55f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                )
        ) {

            if (value.isEmpty()) {
                Text(
                    text = "Start writing your thoughts...",
                    color = TextMuted,
                    fontSize = 16.sp
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        EditorToolbar()
    }
}

// ============================================================================
// EDITOR TOOLBAR
// ============================================================================

@Composable
private fun EditorToolbar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(SurfaceLight.copy(alpha = 0.35f))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        ToolbarIcon(
            icon = {
                Icon(
                    Icons.Default.FormatBold,
                    contentDescription = "Bold"
                )
            }
        )

        ToolbarIcon(
            icon = {
                Icon(
                    Icons.Default.FormatItalic,
                    contentDescription = "Italic"
                )
            }
        )

        ToolbarIcon(
            icon = {
                Icon(
                    Icons.Default.FormatUnderlined,
                    contentDescription = "Underline"
                )
            }
        )

        ToolbarIcon(
            icon = {
                Icon(
                    Icons.Default.FormatListBulleted,
                    contentDescription = "Bullets"
                )
            }
        )

        ToolbarIcon(
            icon = {
                Icon(
                    Icons.Default.FormatAlignLeft,
                    contentDescription = "Align"
                )
            }
        )

        ToolbarIcon(
            icon = {
                Icon(
                    Icons.Default.Image,
                    contentDescription = "Image"
                )
            }
        )
    }
}

@Composable
private fun ToolbarIcon(
    icon: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(11.dp))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

// ============================================================================
// COLOR SELECTOR
// ============================================================================

@Composable
private fun ColorSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {

    val colors = listOf(
        "#7C5CFC" to NotePurple,
        "#FF4FD8" to Color(0xFFFF4FD8),
        "#55EFC4" to NoteMint,
        "#FDCB6E" to NoteYellow,
        "#247BFF" to Color(0xFF247BFF),
        "#636778" to NoteGray
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        colors.forEach { (hex, color) ->

            val isSelected = selectedColor.equals(
                hex,
                ignoreCase = true
            )

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color)
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = 2.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(15.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        onColorSelected(hex)
                    },
                contentAlignment = Alignment.Center
            ) {

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// HELPERS
// ============================================================================

private fun parseNoteColor(
    selectedColor: String,
    fallback: Color
): Color {
    return try {
        Color(
            android.graphics.Color.parseColor(
                selectedColor
            )
        )
    } catch (_: Exception) {
        fallback
    }
}

@Composable
private fun PrimaryLightSafe(): Color {
    return NoteLavender
}