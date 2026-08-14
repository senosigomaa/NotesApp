package com.senosi.notes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.senosi.notes.ui.screens.AddEditNoteScreen
import com.senosi.notes.ui.screens.HomeScreen
import com.senosi.notes.ui.screens.NoteDetailsScreen
import com.senosi.notes.viewmodel.NotesViewModel

object Routes {
    const val HOME = "home"
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

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        // ============================================================
        // HOME
        // ============================================================

        composable(Routes.HOME) {

            HomeScreen(
                notes = notes,

                onMenuClick = {
                    // هنركب الـ Drawer هنا
                },

                onSearchClick = {
                    // Search بعدين
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

                    viewModel.toggleFavorite(note)
                }
            )
        }

        // ============================================================
        // ADD NOTE
        // ============================================================

        composable(Routes.ADD_NOTE) {

            AddEditNoteScreen(
                note = null,

                onBackClick = {
                    navController.popBackStack()
                },

                onSave = { title, content, color ->

                    viewModel.addNote(
                        title = title,
                        content = content,
                        color = color
                    )

                    navController.popBackStack()
                }
            )
        }

        // ============================================================
        // NOTE DETAILS
        // ============================================================

        composable(
            route = "${Routes.NOTE_DETAILS}/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val noteId =
                backStackEntry.arguments?.getLong("noteId")

            val note by viewModel
                .observeNote(noteId ?: -1L)
                .collectAsState(initial = null)

            note?.let {

                NoteDetailsScreen(
                    note = it,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onEditClick = {
                        // هنضيف Edit navigation هنا
                    },

                    onFavoriteClick = {
                        viewModel.toggleFavorite(it)
                    },

                    onDeleteClick = {
                        viewModel.moveToTrash(it)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}