package com.senosi.notes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NotesDarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,

    primaryContainer = SurfaceLight,
    onPrimaryContainer = TextPrimary,

    secondary = Accent,
    onSecondary = Background,

    secondaryContainer = SurfaceLight,
    onSecondaryContainer = TextPrimary,

    tertiary = Cyan,
    onTertiary = Background,

    tertiaryContainer = SurfaceLight,
    onTertiaryContainer = TextPrimary,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,

    error = Danger,
    onError = TextPrimary,

    errorContainer = SurfaceLight,
    onErrorContainer = Danger
)

@Composable
fun NotesAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NotesDarkColorScheme,
        typography = Typography,
        content = content
    )
}