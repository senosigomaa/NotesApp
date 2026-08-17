package com.senosi.notes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.ui.navigation.Routes
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary

@Composable
fun NotesDrawer(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceLight.copy(alpha = 0.98f),
                        Background
                    )
                )
            )
            .padding(
                start = 18.dp,
                end = 18.dp,
                top = 28.dp,
                bottom = 24.dp
            )
    ) {

        // ============================================================
        // HEADER
        // ============================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Primary,
                                Color(0xFFB84DFF)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White,
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
                    text = "My Notes",
                    color = TextPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Your personal space",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "✦",
                color = Primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ============================================================
        // PROFILE CARD
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Surface,
                            SurfaceLight.copy(alpha = 0.35f)
                        )
                    )
                )
                .clickable {
                    onClose()
                }
                .padding(
                    horizontal = 15.dp,
                    vertical = 14.dp
                )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Primary.copy(alpha = 0.13f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "S",
                        color = Primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "My Account",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Premium Member ✦",
                        color = Primary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = "MAIN",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(
                start = 10.dp,
                bottom = 9.dp
            )
        )

        // ============================================================
        // MAIN NAVIGATION
        // ============================================================

        DrawerItem(
            selected = currentRoute == Routes.HOME,
            icon = Icons.Default.Edit,
            label = "Notes",
            onClick = {
                onNavigate(Routes.HOME)
            }
        )

        DrawerItem(
            selected = currentRoute == Routes.CALENDAR,
            icon = Icons.Default.CalendarMonth,
            label = "Calendar",
            onClick = {
                onNavigate(Routes.CALENDAR)
            }
        )

        DrawerItem(
            selected = currentRoute == Routes.STATS,
            icon = Icons.Default.BarChart,
            label = "Statistics",
            onClick = {
                onNavigate(Routes.STATS)
            }
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "MANAGE",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(
                start = 10.dp,
                bottom = 9.dp
            )
        )

        // ============================================================
        // FAVORITES
        // ============================================================

        DrawerItem(
            selected = false,
            icon = Icons.Outlined.StarBorder,
            label = "Favorites",
            iconTint = Primary,
            onClick = {

                onNavigate(

                    Routes.FAVORITES

                )

            }

        )

        // ============================================================
        // TRASH
        // ============================================================

        DrawerItem(
            selected = currentRoute == Routes.TRASH,
            icon = Icons.Default.DeleteOutline,
            label = "Trash",
            iconTint = Color(0xFFFF4D6D),
            onClick = {
                onNavigate(Routes.TRASH)
            }
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // ============================================================
        // SETTINGS
        // ============================================================

        DrawerItem(
            selected = currentRoute == Routes.SETTINGS,
            icon = Icons.Default.Settings,
            label = "Settings",
            onClick = {
                onNavigate(Routes.SETTINGS)
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Notes App • v1.0.0",
            color = TextMuted,
            fontSize = 10.sp,
            modifier = Modifier.padding(
                start = 10.dp
            )
        )
    }
}

@Composable
private fun DrawerItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    iconTint: Color = if (selected) {
        Primary
    } else {
        TextSecondary
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(17.dp)
            )
            .background(
                if (selected) {
                    Primary.copy(alpha = 0.12f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 13.dp,
                vertical = 13.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(
                    RoundedCornerShape(13.dp)
                )
                .background(
                    if (selected) {
                        Primary.copy(alpha = 0.13f)
                    } else {
                        Surface.copy(alpha = 0.7f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Text(
            text = label,
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

        if (selected) {

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Primary)
            )
        }
    }
}