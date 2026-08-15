package com.senosi.notes.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary

@Composable
fun AppBottomNavigation(
    selectedRoute: String,
    onNotesClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onAddClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 14.dp,
                end = 14.dp,
                bottom = 8.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(27.dp)
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SurfaceLight.copy(alpha = 0.92f),
                            Surface.copy(alpha = 0.99f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = SurfaceLight.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(27.dp)
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AppBottomItem(
                selected = selectedRoute == Routes.HOME,
                icon = Icons.Default.Edit,
                label = "Notes",
                onClick = onNotesClick,
                modifier = Modifier.weight(1f)
            )

            AppBottomItem(
                selected = selectedRoute == Routes.CALENDAR,
                icon = Icons.Default.CalendarMonth,
                label = "Calendar",
                onClick = onCalendarClick,
                modifier = Modifier.weight(1f)
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp)
            )

            AppBottomItem(
                selected = selectedRoute == Routes.STATS,
                icon = Icons.Default.BarChart,
                label = "Stats",
                onClick = onStatsClick,
                modifier = Modifier.weight(1f)
            )

            AppBottomItem(
                selected = selectedRoute == Routes.SETTINGS,
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = onSettingsClick,
                modifier = Modifier.weight(1f)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = CircleShape,
                    ambientColor = Primary.copy(alpha = 0.45f),
                    spotColor = Primary.copy(alpha = 0.70f)
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Primary,
                            Color(0xFFB84DFF)
                        )
                    )
                )
                .clickable(
                    onClick = onAddClick
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add note",
                tint = Color.White,
                modifier = Modifier.size(27.dp)
            )
        }
    }
}

@Composable
private fun AppBottomItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(18.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                vertical = 7.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            modifier = Modifier.size(3.dp)
        )

        Text(
            text = label,
            color = if (selected) {
                TextPrimary
            } else {
                TextSecondary
            },
            fontSize = 10.sp
        )

        if (selected) {
            Spacer(
                modifier = Modifier.size(2.dp)
            )

            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Primary)
            )
        }
    }
}