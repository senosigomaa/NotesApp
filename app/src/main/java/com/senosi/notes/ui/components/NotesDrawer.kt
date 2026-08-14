package com.senosi.notes.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.ui.theme.PrimaryPurple

@Composable
fun NotesDrawer(
    selectedItem: String = "All Notes",
    onItemClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.82f)
            .background(Color(0xFFFBFAFF))
            .padding(20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Profile
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(
                        PrimaryPurple.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "A",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Column {

                Text(
                    text = "Ahmed Mohamed",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF29292E)
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "ahmed@example.com",
                    fontSize = 12.sp,
                    color = Color(0xFF858590)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(35.dp)
        )

        DrawerItem(
            icon = Icons.Default.Notes,
            title = "All Notes",
            selected = selectedItem == "All Notes",
            onClick = {
                onItemClick("All Notes")
            }
        )

        DrawerItem(
            icon = Icons.Default.Star,
            title = "Favorites",
            selected = selectedItem == "Favorites",
            onClick = {
                onItemClick("Favorites")
            }
        )

        DrawerItem(
            icon = Icons.Default.DeleteOutline,
            title = "Trash",
            selected = selectedItem == "Trash",
            onClick = {
                onItemClick("Trash")
            }
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        DrawerItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            selected = selectedItem == "Settings",
            onClick = {
                onItemClick("Settings")
            }
        )

        DrawerItem(
            icon = Icons.Default.Info,
            title = "About Us",
            selected = selectedItem == "About Us",
            onClick = {
                onItemClick("About Us")
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Notes App",
            fontSize = 12.sp,
            color = Color(0xFF9A9AA5),
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        )

        Text(
            text = "Version 1.0",
            fontSize = 11.sp,
            color = Color(0xFFB0B0B8),
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 3.dp
            )
        )
    }
}

@Composable
private fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val background =
        if (selected) {
            PrimaryPurple.copy(alpha = 0.10f)
        } else {
            Color.Transparent
        }

    val iconColor =
        if (selected) {
            PrimaryPurple
        } else {
            Color(0xFF686873)
        }

    val textColor =
        if (selected) {
            PrimaryPurple
        } else {
            Color(0xFF424249)
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(background)
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 15.dp,
                vertical = 14.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(21.dp)
        )

        Spacer(
            modifier = Modifier.size(15.dp)
        )

        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight =
                if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
            color = textColor
        )
    }

    Spacer(
        modifier = Modifier.height(4.dp)
    )
}