package com.senosi.notes.ui.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.senosi.notes.ui.screens.AddEditNoteScreen
import com.senosi.notes.ui.screens.CalendarScreen
import com.senosi.notes.ui.screens.HomeScreen
import com.senosi.notes.ui.screens.NoteDetailsScreen
import com.senosi.notes.ui.screens.SettingsScreen
import com.senosi.notes.ui.screens.StatisticsScreen
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.viewmodel.NotesViewModel

object Routes {

    const val HOME = "home"

    const val CALENDAR = "calendar"

    const val STATS = "stats"

    const val SETTINGS = "SettingsScreen"

    const val ADD_NOTE = "add_note"

    const val NOTE_DETAILS = "note_details"
}

@Composable
fun AppNavigation(
    viewModel: NotesViewModel = viewModel()
) {
    val navController = rememberNavController()

    val notes by viewModel
        .observeNotes()
        .collectAsState(initial = emptyList())

    val backStackEntry by navController
        .currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    val topLevelRoutes = setOf(
        Routes.HOME,
        Routes.CALENDAR,
        Routes.STATS,
        Routes.SETTINGS
    )

    val showBottomNavigation =
        currentRoute in topLevelRoutes

    fun navigateToTopLevel(route: String) {

        if (currentRoute == route) {
            return
        }

        navController.navigate(route) {

            popUpTo(Routes.HOME) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }
    }

    Scaffold(
        containerColor = Background,

        bottomBar = {

            if (showBottomNavigation) {

                AppBottomNavigation(
                    selectedRoute =
                        currentRoute
                            ?: Routes.HOME,

                    onNotesClick = {
                        navigateToTopLevel(
                            Routes.HOME
                        )
                    },

                    onCalendarClick = {
                        navigateToTopLevel(
                            Routes.CALENDAR
                        )
                    },

                    onAddClick = {
                        navController.navigate(
                            Routes.ADD_NOTE
                        )
                    },

                    onStatsClick = {
                        navigateToTopLevel(
                            Routes.STATS
                        )
                    },

                    onSettingsClick = {
                        navigateToTopLevel(
                            Routes.SETTINGS
                        )
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ========================================================
            // HOME
            // ========================================================

            composable(Routes.HOME) {

                HomeScreen(
                    notes = notes,

                    onMenuClick = {
                        // Drawer لاحقاً
                    },

                    onSearchClick = {
                        // Search لاحقاً
                    },

                    onAddNoteClick = {
                        navController.navigate(
                            Routes.ADD_NOTE
                        )
                    },

                    onNoteClick = { noteId ->

                        navController.navigate(
                            "${Routes.NOTE_DETAILS}/$noteId"
                        )
                    },

                    onFavoriteClick = { note ->

                        viewModel.toggleFavorite(
                            note
                        )
                    }
                )
            }

            // ========================================================
            // CALENDAR
            // ========================================================

            composable(Routes.CALENDAR) {

                CalendarScreen(
                    onBackClick = {

                        navigateToTopLevel(
                            Routes.HOME
                        )
                    },

                    onAddEventClick = {
                        // إضافة Event لاحقاً
                    }
                )
            }

            // ========================================================
            // STATISTICS
            // ========================================================

            composable(Routes.STATS) {

                StatisticsScreen(
                    notes = notes
                )
            }

            // ========================================================
            // SETTINGS
            // ========================================================

            composable(Routes.SETTINGS) {

                SettingsScreen(
                    onBackClick = {
                        navigateToTopLevel(
                            Routes.HOME
                        )
                    },

                    onProfileClick = {
                        // Profile لاحقاً
                    },

                    onUpgradeClick = {
                        // Premium لاحقاً
                    },

                    onAppearanceClick = {
                        // Appearance
                    },

                    onAccentColorClick = {
                        // Accent Color
                    },

                    onFontStyleClick = {
                        // Font Style
                    },

                    onNoteViewClick = {
                        // Note View
                    },

                    onBackupClick = {
                        // Backup & Sync
                    },

                    onSecurityClick = {
                        // Security
                    },

                    onHelpClick = {
                        // Help & Support
                    },

                    onAboutClick = {
                        // About App
                    }
                )
            }
            // ========================================================
            // ADD NOTE
            // ========================================================

            composable(Routes.ADD_NOTE) {

                AddEditNoteScreen(
                    note = null,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onSave = {
                            title,
                            content,
                            color ->

                        viewModel.addNote(
                            title = title,
                            content = content,
                            color = color
                        )

                        navController.popBackStack()
                    }
                )
            }

            // ========================================================
            // NOTE DETAILS
            // ========================================================

            composable(
                route =
                    "${Routes.NOTE_DETAILS}/{noteId}",

                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val noteId =
                    backStackEntry
                        .arguments
                        ?.getLong("noteId")

                val note by viewModel
                    .observeNote(
                        noteId ?: -1L
                    )
                    .collectAsState(
                        initial = null
                    )

                note?.let {

                    NoteDetailsScreen(
                        note = it,

                        onBackClick = {
                            navController.popBackStack()
                        },

                        onEditClick = {
                            // Edit لاحقاً
                        },

                        onFavoriteClick = {
                            viewModel.toggleFavorite(
                                it
                            )
                        },

                        onDeleteClick = {

                            viewModel.moveToTrash(
                                it
                            )

                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

