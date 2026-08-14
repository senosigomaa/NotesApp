package com.senosi.notes.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.senosi.notes.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    notes: List<Note>,
    onBackClick: () -> Unit,
    onNoteClick: (Long) -> Unit
) {

    var query by remember {
        mutableStateOf("")
    }

    val results = notes.filter {
        it.title.contains(query, true) ||
                it.content.contains(query, true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Search")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search...")
                },
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(results) { note ->

                    Text(
                        text = note.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .then(
                                Modifier
                            )
                    )
                }
            }
        }
    }
}