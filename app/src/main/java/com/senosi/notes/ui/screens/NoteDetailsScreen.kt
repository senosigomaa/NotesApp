package com.senosi.notes.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.Accent
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Danger
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteDetailsScreen(
    note: Note,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    var moreMenuExpanded by remember {
        mutableStateOf(false)
    }

    val noteColor = remember(note.color) {
        runCatching {
            Color(
                android.graphics.Color.parseColor(note.color)
            )
        }.getOrDefault(Primary)
    }

    val formattedDate = remember(note.updatedAt) {
        SimpleDateFormat(
            "MMM dd, yyyy • h:mm a",
            Locale.getDefault()
        ).format(Date(note.updatedAt))
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NoteDetailsBottomBar(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 18.dp
                )
        ) {

            /*
             * ---------------------------------------------------------
             * TOP BAR
             * ---------------------------------------------------------
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                DetailIconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                DetailIconButton(
                    onClick = onFavoriteClick
                ) {
                    Icon(
                        imageVector = if (note.isFavorite) {
                            Icons.Default.Star
                        } else {
                            Icons.Outlined.StarBorder
                        },
                        contentDescription = "Favorite",
                        tint = if (note.isFavorite) {
                            Primary
                        } else {
                            TextSecondary
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                DetailIconButton(
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Box {
                    DetailIconButton(
                        onClick = {
                            moreMenuExpanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "More",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = moreMenuExpanded,
                        onDismissRequest = {
                            moreMenuExpanded = false
                        },
                        containerColor = Surface
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit Note",
                                    color = TextPrimary
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Primary
                                )
                            },
                            onClick = {
                                moreMenuExpanded = false
                                onEditClick()
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Move to Trash",
                                    color = Danger
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Danger
                                )
                            },
                            onClick = {
                                moreMenuExpanded = false
                                onDeleteClick()
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            /*
             * ---------------------------------------------------------
             * META ROW
             * ---------------------------------------------------------
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CategoryBadge(
                    color = noteColor
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formattedDate,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            /*
             * ---------------------------------------------------------
             * TITLE
             * ---------------------------------------------------------
             */

            GradientTitle(
                text = note.title.ifBlank {
                    "Untitled Note"
                }
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            NeonTitleDivider()

            Spacer(
                modifier = Modifier.height(38.dp)
            )

            /*
             * ---------------------------------------------------------
             * CONTENT
             * ---------------------------------------------------------
             */

            Text(
                text = note.content.ifBlank {
                    "No content in this note."
                },
                color = TextSecondary,
                fontSize = 20.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            /*
             * ---------------------------------------------------------
             * KEY POINTS
             * ---------------------------------------------------------
             *
             * We don't invent key points because the Note model
             * currently has only title/content/color/etc.
             *
             * Instead, this section gives the screen the same visual
             * rhythm without corrupting the data.
             */

            SectionHeader(
                title = "Note Overview",
                icon = "✦"
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            NoteOverview(
                note = note,
                color = noteColor
            )

            Spacer(
                modifier = Modifier.height(38.dp)
            )

            /*
             * ---------------------------------------------------------
             * DECORATIVE DIVIDER
             * ---------------------------------------------------------
             */

            WaveDivider()

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            /*
             * ---------------------------------------------------------
             * ATTACHMENTS
             * ---------------------------------------------------------
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Attachments",
                    color = Primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.material3.ButtonDefaults.outlinedButtonBorder,
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Text(
                        text = "View All",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AttachmentEmptyState()

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            /*
             * ---------------------------------------------------------
             * DECORATIVE WAVE
             * ---------------------------------------------------------
             */

            DecorativeWave()

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

/*
 * ================================================================
 * TOP ICON BUTTON
 * ================================================================
 */

@Composable
private fun DetailIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        SurfaceLight,
                        Surface
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/*
 * ================================================================
 * CATEGORY BADGE
 * ================================================================
 */

@Composable
private fun CategoryBadge(
    color: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color.copy(alpha = 0.10f)
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.20f),
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = "Personal",
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/*
 * ================================================================
 * GRADIENT TITLE
 * ================================================================
 */

@Composable
private fun GradientTitle(
    text: String
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 40.sp,
            lineHeight = 46.sp,
            fontWeight = FontWeight.Bold,
            brush = Brush.linearGradient(
                colors = listOf(
                    Primary,
                    Accent,
                    Color(0xFFFF4FD8)
                )
            )
        )
    )
}

/*
 * ================================================================
 * NEON DIVIDER
 * ================================================================
 */

@Composable
private fun NeonTitleDivider() {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Primary,
                        Color(0xFFFF4FD8),
                        Accent
                    )
                )
            )
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(50)
            )
    )
}

/*
 * ================================================================
 * SECTION HEADER
 * ================================================================
 */

@Composable
private fun SectionHeader(
    title: String,
    icon: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            color = Primary,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Text(
            text = title,
            color = Primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/*
 * ================================================================
 * NOTE OVERVIEW
 * ================================================================
 */

@Composable
private fun NoteOverview(
    note: Note,
    color: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        OverviewRow(
            label = "Status",
            value = if (note.isFavorite) {
                "Favorite"
            } else {
                "Regular note"
            },
            color = if (note.isFavorite) {
                Accent
            } else {
                TextSecondary
            }
        )

        OverviewRow(
            label = "Color",
            value = note.color.uppercase(),
            color = color
        )

        OverviewRow(
            label = "Content",
            value = "${note.content.length} characters",
            color = TextSecondary
        )
    }
}

@Composable
private fun OverviewRow(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Text(
            text = label,
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/*
 * ================================================================
 * WAVE DIVIDER
 * ================================================================
 */

@Composable
private fun WaveDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {

        val centerY = size.height / 2f

        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    Primary.copy(alpha = 0.35f)
                )
            ),
            start = Offset(0f, centerY),
            end = Offset(size.width * 0.42f, centerY),
            strokeWidth = 1.5f
        )

        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Primary.copy(alpha = 0.35f),
                    Color.Transparent
                )
            ),
            start = Offset(size.width * 0.58f, centerY),
            end = Offset(size.width, centerY),
            strokeWidth = 1.5f
        )

        val path = Path().apply {
            moveTo(
                size.width * 0.44f,
                centerY
            )

            cubicTo(
                size.width * 0.46f,
                centerY - 8f,
                size.width * 0.48f,
                centerY + 8f,
                size.width * 0.50f,
                centerY
            )

            cubicTo(
                size.width * 0.52f,
                centerY - 8f,
                size.width * 0.54f,
                centerY + 8f,
                size.width * 0.56f,
                centerY
            )
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Primary,
                    Color(0xFFFF4FD8)
                )
            ),
            style = Stroke(width = 3f)
        )
    }
}

/*
 * ================================================================
 * ATTACHMENT EMPTY STATE
 * ================================================================
 */

@Composable
private fun AttachmentEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(alpha = 0.55f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Primary.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AttachFile,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "No attachments",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Files and images will appear here",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}

/*
 * ================================================================
 * DECORATIVE NEON WAVE
 * ================================================================
 */

@Composable
private fun DecorativeWave() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {

        repeat(4) { index ->

            val path = Path()

            val yOffset =
                size.height * 0.30f +
                        (index * 10f)

            path.moveTo(
                0f,
                yOffset
            )

            path.cubicTo(
                size.width * 0.18f,
                yOffset - 22f,
                size.width * 0.32f,
                yOffset + 22f,
                size.width * 0.50f,
                yOffset
            )

            path.cubicTo(
                size.width * 0.68f,
                yOffset - 22f,
                size.width * 0.82f,
                yOffset + 22f,
                size.width,
                yOffset
            )

            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Primary.copy(
                            alpha = 0.45f - index * 0.07f
                        ),
                        Color(0xFFFF4FD8).copy(
                            alpha = 0.25f
                        ),
                        Color.Transparent
                    )
                ),
                style = Stroke(
                    width = 1.2f
                )
            )
        }
    }
}

/*
 * ================================================================
 * BOTTOM ACTION BAR
 * ================================================================
 */

@Composable
private fun NoteDetailsBottomBar(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Background.copy(alpha = 0.98f)
            )
            .navigationBarsPadding()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 12.dp,
                bottom = 14.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = onEditClick,
            modifier = Modifier
                .weight(1f)
                .height(62.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(22.dp)
                ),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(21.dp)
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Text(
                text = "Edit Note",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .size(62.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Danger.copy(alpha = 0.08f)
                )
                .border(
                    width = 1.dp,
                    color = Danger.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(22.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Move to trash",
                tint = Danger,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}