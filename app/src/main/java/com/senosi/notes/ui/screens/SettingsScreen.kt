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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary

// ============================================================================
// SETTINGS SCREEN
// ============================================================================

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onUpgradeClick: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
    onAccentColorClick: () -> Unit = {},
    onFontStyleClick: () -> Unit = {},
    onNoteViewClick: () -> Unit = {},
    onBackupClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    var remindersEnabled by remember {
        mutableStateOf(true)
    }

    var noteViewGrid by remember {
        mutableStateOf(false)
    }

    var showAppearanceDialog by remember {
        mutableStateOf(false)
    }

    var showAccentDialog by remember {
        mutableStateOf(false)
    }

    var selectedAppearance by remember {
        mutableStateOf("Dark")
    }

    var selectedAccent by remember {
        mutableStateOf("Purple")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 18.dp,
            bottom = 120.dp
        ),

        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // ====================================================================
        // HEADER
        // ====================================================================

        item {

            SettingsHeader(
                onBackClick = onBackClick
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        // ====================================================================
        // PROFILE
        // ====================================================================

        item {

            SettingsProfileCard(
                onClick = onProfileClick
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        // ====================================================================
        // PREMIUM
        // ====================================================================

        item {

            PremiumCard(
                onClick = onUpgradeClick
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(22.dp)
            )
        }

        // ====================================================================
        // PREFERENCES
        // ====================================================================

        item {

            SettingsSectionTitle(
                text = "Preferences"
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(9.dp)
            )
        }

        item {

            SettingsGroup(
                items = listOf(
                    SettingsRowData(
                        icon = Icons.Default.DarkMode,
                        iconColor = Primary,
                        title = "Appearance",
                        subtitle = "Choose your theme",
                        value = selectedAppearance
                    ),
                    SettingsRowData(
                        icon = Icons.Default.Palette,
                        iconColor = Color(0xFFFF4FD8),
                        title = "Accent Color",
                        subtitle = "Pick your favorite color",
                        value = selectedAccent
                    ),
                    SettingsRowData(
                        icon = Icons.Default.TextFields,
                        iconColor = Primary,
                        title = "Font Style",
                        subtitle = "Adjust font preferences",
                        value = "Poppins"
                    )
                ),

                onRowClick = { index ->

                    when (index) {

                        0 -> {
                            showAppearanceDialog = true
                            onAppearanceClick()
                        }

                        1 -> {
                            showAccentDialog = true
                            onAccentColorClick()
                        }

                        2 -> {
                            onFontStyleClick()
                        }
                    }
                }
            )
        }

        item {
            SettingsGroup(
                items = listOf(
                    SettingsRowData(
                        icon = if (noteViewGrid) {
                            Icons.Default.GridView
                        } else {
                            Icons.Default.ViewList
                        },
                        iconColor = Color(0xFF00D9D9),
                        title = "Note View",
                        subtitle = "Choose default note layout"
                    )
                ),
                showDivider = false,
                trailingContent = {
                    NoteViewSelector(
                        gridSelected = noteViewGrid,
                        onListClick = {
                            noteViewGrid = false
                            onNoteViewClick()
                        },
                        onGridClick = {
                            noteViewGrid = true
                            onNoteViewClick()
                        }
                    )
                },
                onRowClick = {
                    noteViewGrid = !noteViewGrid
                    onNoteViewClick()
                }
            )
        }

        item {

            SettingsGroup(
                items = listOf(
                    SettingsRowData(
                        icon = Icons.Default.Notifications,
                        iconColor = Color(0xFFFFB800),
                        title = "Reminders",
                        subtitle = "Manage notification settings"
                    )
                ),
                showDivider = false,
                trailingContent = {
                    SettingsSwitch(
                        checked = remindersEnabled,
                        onCheckedChange = {
                            remindersEnabled = it
                        }
                    )
                },
                onRowClick = {
                    remindersEnabled = !remindersEnabled
                }
            )
        }

        item {

            SettingsGroup(
                items = listOf(
                    SettingsRowData(
                        icon = Icons.Default.CloudUpload,
                        iconColor = Color(0xFF00CFE8),
                        title = "Backup & Sync",
                        subtitle = "Keep your notes safe"
                    )
                ),
                showDivider = false,
                onRowClick = {
                    onBackupClick()
                }
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(22.dp)
            )
        }

        // ====================================================================
        // MORE
        // ====================================================================

        item {

            SettingsSectionTitle(
                text = "More"
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(9.dp)
            )
        }

        item {

            SettingsGroup(
                items = listOf(
                    SettingsRowData(
                        icon = Icons.Default.Security,
                        iconColor = Color(0xFF2DEB88),
                        title = "Security",
                        subtitle = "Lock and privacy options"
                    ),
                    SettingsRowData(
                        icon = Icons.Default.HelpOutline,
                        iconColor = Color(0xFF4DA6FF),
                        title = "Help & Support",
                        subtitle = "Get help and contact us"
                    ),
                    SettingsRowData(
                        icon = Icons.Default.Info,
                        iconColor = Color(0xFFAA5CFF),
                        title = "About App",
                        subtitle = "Version 1.0.0"
                    )
                ),

                onRowClick = { index ->

                    when (index) {

                        0 -> onSecurityClick()

                        1 -> onHelpClick()

                        2 -> onAboutClick()
                    }
                }
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        // ====================================================================
        // FOOTER
        // ====================================================================

        item {

            SettingsFooter()
        }
    }

    // ========================================================================
    // APPEARANCE DIALOG
    // ========================================================================

    if (showAppearanceDialog) {

        AppearanceDialog(
            selectedAppearance = selectedAppearance,
            onDismiss = {
                showAppearanceDialog = false
            },
            onSelect = {
                selectedAppearance = it
                showAppearanceDialog = false
            }
        )
    }

    // ========================================================================
    // ACCENT COLOR DIALOG
    // ========================================================================

    if (showAccentDialog) {

        AccentColorDialog(
            selectedAccent = selectedAccent,
            onDismiss = {
                showAccentDialog = false
            },
            onSelect = {
                selectedAccent = it
                showAccentDialog = false
            }
        )
    }
}

// ============================================================================
// HEADER
// ============================================================================

@Composable
private fun SettingsHeader(
    onBackClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

//        SettingsCircleButton(
//            icon = Icons.Default.ArrowBack,
//            tint = Primary,
//            onClick = onBackClick
//        )

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Settings",
                    color = TextPrimary,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "✦",
                    color = Primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "Customize your experience",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }

        SettingsCircleButton(
            icon = Icons.Default.Settings,
            tint = Primary,
            onClick = {}
        )
    }
}

// ============================================================================
// CIRCLE BUTTON
// ============================================================================

@Composable
private fun SettingsCircleButton(
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        SurfaceLight.copy(alpha = 0.90f),
                        Surface.copy(alpha = 0.96f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.18f),
                shape = CircleShape
            )
            .clickable(
                onClick = onClick
            ),

        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(23.dp)
        )
    }
}

// ============================================================================
// PROFILE CARD
// ============================================================================

@Composable
private fun SettingsProfileCard(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(21.dp)
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(alpha = 0.45f),
                        Surface
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight.copy(alpha = 0.75f),
                shape = RoundedCornerShape(21.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Primary.copy(alpha = 0.30f),
                            Color(0xFFB84DFF).copy(alpha = 0.12f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Primary.copy(alpha = 0.30f),
                    shape = CircleShape
                ),

            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(31.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Senosi Gomaa",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Premium Member",
                    color = Color(0xFFB05CFF),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "♛",
                    color = Primary,
                    fontSize = 15.sp
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(25.dp)
        )
    }
}

// ============================================================================
// PREMIUM CARD
// ============================================================================

@Composable
private fun PremiumCard(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF19152B),
                        Color(0xFF21133A),
                        Color(0xFF171A2B)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.42f),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.30f),
                                Color(0xFFFF4FD8).copy(alpha = 0.10f)
                            )
                        )
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Go Premium ✨",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Unlock all features and",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Text(
                    text = "enhance your productivity.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(50.dp),
                        ambientColor = Primary.copy(alpha = 0.35f),
                        spotColor = Primary.copy(alpha = 0.45f)
                    )
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Primary,
                                Color(0xFFC14DFF)
                            )
                        )
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 11.dp
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Upgrade",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ============================================================================
// SECTION TITLE
// ============================================================================

@Composable
private fun SettingsSectionTitle(
    text: String
) {

    Text(
        text = text,
        color = TextSecondary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(
            start = 4.dp
        )
    )
}

// ============================================================================
// SETTINGS DATA
// ============================================================================

private data class SettingsRowData(
    val icon: ImageVector,
    val iconColor: Color,
    val title: String,
    val subtitle: String,
    val value: String? = null
)

// ============================================================================
// SETTINGS GROUP
// ============================================================================

@Composable
private fun SettingsGroup(
    items: List<SettingsRowData>,
    onRowClick: (Int) -> Unit,
    showDivider: Boolean = true,
    trailingContent: @Composable (() -> Unit)? = null
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(21.dp)
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceLight.copy(alpha = 0.28f),
                        Surface.copy(alpha = 0.96f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight.copy(alpha = 0.62f),
                shape = RoundedCornerShape(21.dp)
            )
    ) {

        items.forEachIndexed { index, item ->

            SettingsRow(
                item = item,
                onClick = {
                    onRowClick(index)
                },
                trailingContent = if (
                    trailingContent != null &&
                    items.size == 1
                ) {
                    trailingContent
                } else {
                    null
                }
            )

            if (
                showDivider &&
                index != items.lastIndex
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 76.dp
                        )
                        .height(1.dp)
                        .background(
                            SurfaceLight.copy(alpha = 0.45f)
                        )
                )
            }
        }
    }
}

// ============================================================================
// SETTINGS ROW
// ============================================================================

@Composable
private fun SettingsRow(
    item: SettingsRowData,
    onClick: () -> Unit,
    trailingContent: @Composable (() -> Unit)?
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 14.dp,
                vertical = 13.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIconBox(
            icon = item.icon,
            color = item.iconColor
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = item.title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = item.subtitle,
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (trailingContent != null) {

            trailingContent()

        } else if (item.value != null) {

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(50.dp)
                    )
                    .background(
                        Primary.copy(alpha = 0.08f)
                    )
                    .border(
                        width = 1.dp,
                        color = Primary.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text = item.value,
                    color = Primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(21.dp)
            )

        } else {

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ============================================================================
// SETTINGS ICON
// ============================================================================

@Composable
private fun SettingsIconBox(
    icon: ImageVector,
    color: Color
) {

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        color.copy(alpha = 0.18f),
                        color.copy(alpha = 0.07f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.10f),
                shape = RoundedCornerShape(14.dp)
            ),

        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(23.dp)
        )
    }
}

// ============================================================================
// NOTE VIEW SELECTOR
// ============================================================================

@Composable
private fun NoteViewSelector(
    gridSelected: Boolean,
    onListClick: () -> Unit,
    onGridClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(13.dp)
            )
            .background(
                SurfaceLight.copy(alpha = 0.45f)
            )
            .border(
                width = 1.dp,
                color = SurfaceLight.copy(alpha = 0.65f),
                shape = RoundedCornerShape(13.dp)
            )
            .padding(3.dp)
    ) {

        NoteViewButton(
            selected = !gridSelected,
            icon = Icons.Default.ViewList,
            onClick = onListClick
        )

        NoteViewButton(
            selected = gridSelected,
            icon = Icons.Default.GridView,
            onClick = onGridClick
        )
    }
}

@Composable
private fun NoteViewButton(
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(37.dp)
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                if (selected) {
                    Primary.copy(alpha = 0.18f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                onClick = onClick
            ),

        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) {
                Primary
            } else {
                TextMuted
            },
            modifier = Modifier.size(20.dp)
        )
    }
}

// ============================================================================
// SWITCH
// ============================================================================

@Composable
private fun SettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Primary,
            checkedBorderColor = Primary,
            uncheckedThumbColor = TextSecondary,
            uncheckedTrackColor = SurfaceLight,
            uncheckedBorderColor = SurfaceLight
        )
    )
}

// ============================================================================
// FOOTER
// ============================================================================

@Composable
private fun SettingsFooter() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Senosi Notes",
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Made for better thoughts ✦",
            color = TextMuted.copy(alpha = 0.65f),
            fontSize = 10.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Version 1.0.0",
            color = TextMuted.copy(alpha = 0.55f),
            fontSize = 9.sp
        )
    }
}

// ============================================================================
// APPEARANCE DIALOG
// ============================================================================

@Composable
private fun AppearanceDialog(
    selectedAppearance: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        containerColor = Surface,

        title = {
            Text(
                text = "Appearance",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                AppearanceOption(
                    title = "Dark",
                    selected = selectedAppearance == "Dark",
                    onClick = {
                        onSelect("Dark")
                    }
                )

                AppearanceOption(
                    title = "System",
                    selected = selectedAppearance == "System",
                    onClick = {
                        onSelect("System")
                    }
                )

                AppearanceOption(
                    title = "Light",
                    selected = selectedAppearance == "Light",
                    onClick = {
                        onSelect("Light")
                    }
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Close",
                    color = Primary
                )
            }
        }
    )
}

// ============================================================================
// APPEARANCE OPTION
// ============================================================================

@Composable
private fun AppearanceOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                if (selected) {
                    Primary.copy(alpha = 0.10f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp,
                vertical = 13.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    if (selected) {
                        Primary
                    } else {
                        TextMuted
                    }
                )
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = title,
            color = if (selected) {
                TextPrimary
            } else {
                TextSecondary
            },
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}

// ============================================================================
// ACCENT COLOR DIALOG
// ============================================================================

@Composable
private fun AccentColorDialog(
    selectedAccent: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {

    val colors = listOf(
        "Purple" to Primary,
        "Pink" to Color(0xFFFF4FD8),
        "Cyan" to Color(0xFF00D9D9),
        "Blue" to Color(0xFF4DA6FF),
        "Orange" to Color(0xFFFFB800)
    )

    AlertDialog(
        onDismissRequest = onDismiss,

        containerColor = Surface,

        title = {
            Text(
                text = "Accent Color",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                colors.forEach { (name, color) ->

                    AccentColorOption(
                        name = name,
                        color = color,
                        selected = selectedAccent == name,
                        onClick = {
                            onSelect(name)
                        }
                    )
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Close",
                    color = Primary
                )
            }
        }
    )
}

// ============================================================================
// ACCENT COLOR OPTION
// ============================================================================

@Composable
private fun AccentColorOption(
    name: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                if (selected) {
                    color.copy(alpha = 0.10f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = Color.White,
                    shape = CircleShape
                )
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = name,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}