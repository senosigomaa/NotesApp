package com.senosi.notes.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.senosi.notes.ui.screens.AddEditNoteScreen
import com.senosi.notes.ui.screens.HomeScreen
import com.senosi.notes.ui.screens.NoteDetailsScreen
import com.senosi.notes.ui.screens.SearchScreen
import com.senosi.notes.ui.screens.SimpleScreen
import com.senosi.notes.ui.screens.SplashScreen
import com.senosi.notes.viewmodel.NotesViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    viewModel: NotesViewModel = viewModel()
) {

    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val notes by viewModel
        .observeNotes()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val favorites by viewModel
        .observeFavorites()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val trash by viewModel
        .observeTrash()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Ahmed Mohamed"
                    )

                    Text(
                        text = "ahmed@example.com"
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("All Notes")
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }

                            navController.navigate("home")
                        }
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("Favorites")
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }

                            navController.navigate("favorites")
                        }
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("Trash")
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }

                            navController.navigate("trash")
                        }
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("Settings")
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }

                            navController.navigate("settings")
                        }
                    )

                    NavigationDrawerItem(
                        label = {
                            Text("About Us")
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }

                            navController.navigate("about")
                        }
                    )
                }
            }
        }
    ) {

        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {

            composable("splash") {

                SplashScreen()

                androidx.compose.runtime.LaunchedEffect(Unit) {

                    kotlinx.coroutines.delay(1200)

                    navController.navigate("home") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            }

            composable("home") {

                HomeScreen(
                    notes = notes,

                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },

                    onSearchClick = {
                        navController.navigate("search")
                    },

                    onAddNoteClick = {
                        navController.navigate("add_note")
                    },

                    onNoteClick = { id ->
                        navController.navigate("details/$id")
                    },

                    onFavoriteClick = { note ->
                        viewModel.toggleFavorite(note)
                    }
                )
            }

            composable("favorites") {

                HomeScreen(
                    notes = favorites,

                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },

                    onSearchClick = {
                        navController.navigate("search")
                    },

                    onAddNoteClick = {
                        navController.navigate("add_note")
                    },

                    onNoteClick = { id ->
                        navController.navigate("details/$id")
                    },

                    onFavoriteClick = { note ->
                        viewModel.toggleFavorite(note)
                    }
                )
            }

            composable("trash") {

                HomeScreen(
                    notes = trash,

                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },

                    onSearchClick = {
                        navController.navigate("search")
                    },

                    onAddNoteClick = {
                        navController.navigate("add_note")
                    },

                    onNoteClick = { id ->
                        navController.navigate("details/$id")
                    },

                    onFavoriteClick = {}
                )
            }

            composable("add_note") {

                AddEditNoteScreen(
                    note = null,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onSave = { title, content, color ->

                        if (title.isNotBlank() || content.isNotBlank()) {

                            viewModel.addNote(
                                title = title.ifBlank {
                                    "Untitled"
                                },
                                content = content,
                                color = color
                            )

                            navController.popBackStack()
                        }
                    }
                )
            }

            composable("edit_note/{id}") { backStackEntry ->

                val id = backStackEntry.arguments
                    ?.getString("id")
                    ?.toLongOrNull()

                val note by if (id != null) {
                    viewModel.observeNote(id)
                        .collectAsStateWithLifecycle(initialValue = null)
                } else {
                    androidx.compose.runtime.remember {
                        androidx.compose.runtime.mutableStateOf(null)
                    }
                }

                AddEditNoteScreen(
                    note = note,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onSave = { title, content, color ->

                        if (note != null) {

                            viewModel.updateNote(
                                note!!.copy(
                                    title = title,
                                    content = content,
                                    color = color
                                )
                            )

                            navController.popBackStack()
                        }
                    }
                )
            }

            composable("details/{id}") { backStackEntry ->

                val id = backStackEntry.arguments
                    ?.getString("id")
                    ?.toLongOrNull()

                if (id != null) {

                    val note by viewModel
                        .observeNote(id)
                        .collectAsStateWithLifecycle(
                            initialValue = null
                        )

                    if (note != null) {

                        NoteDetailsScreen(

                            note = note!!,

                            onBackClick = {
                                navController.popBackStack()
                            },

                            onEditClick = {
                                navController.navigate(
                                    "edit_note/$id"
                                )
                            },

                            onFavoriteClick = {
                                viewModel.toggleFavorite(note!!)
                            },

                            onDeleteClick = {

                                viewModel.moveToTrash(
                                    note!!
                                )

                                navController.popBackStack()
                            }
                        )
                    }
                }
            }

            composable("search") {

                SearchScreen(
                    notes = notes,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNoteClick = { id ->
                        navController.navigate(
                            "details/$id"
                        )
                    }
                )
            }

            composable("settings") {
                SimpleScreen("Settings")
            }

            composable("about") {
                SimpleScreen("About Us")
            }
        }
    }
}