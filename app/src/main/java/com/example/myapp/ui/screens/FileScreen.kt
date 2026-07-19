package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapp.ui.components.FileItemRow
import com.example.myapp.ui.enums.FileUiState
import com.example.myapp.ui.enums.UserState
import com.example.myapp.ui.viewModels.FileViewModel
import com.example.myapp.ui.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileScreen(
    fileViewModel: FileViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val uiState by fileViewModel.uiState.collectAsState()
    val userState by userViewModel.userState.collectAsState()
    
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var folderName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mes Fichiers")
                        when (val state = userState) {
                            is UserState.Success -> {
                                Text(
                                    text = "${state.user.firstName} ${state.user.lastName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            is UserState.Loading -> {
                                Text(
                                    text = "Chargement utilisateur...",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            is UserState.Error -> {
                                Text(
                                    text = "Erreur profil",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { fileViewModel.loadRoot() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateFolderDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Folder")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is FileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is FileUiState.Success -> {
                    if (state.files.isEmpty()) {
                        Text(
                            text = "Dossier vide",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.files) { file ->
                                FileItemRow(
                                    fileItem = file,
                                    onItemClick = {
                                        if (it.isDirectory) {
                                            fileViewModel.loadFiles(it.id)
                                        }
                                    },
                                    onDeleteClick = {
                                        fileViewModel.deleteItem(it.id)
                                    }
                                )
                            }
                        }
                    }
                }
                is FileUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Erreur: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { fileViewModel.loadRoot() }) {
                            Text("Réessayer")
                        }
                    }
                }
            }
        }

        if (showCreateFolderDialog) {
            AlertDialog(
                onDismissRequest = { showCreateFolderDialog = false },
                title = { Text("Nouveau dossier") },
                text = {
                    TextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        label = { Text("Nom du dossier") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (folderName.isNotBlank()) {
                            fileViewModel.createFolder(folderName)
                            folderName = ""
                            showCreateFolderDialog = false
                        }
                    }) {
                        Text("Créer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateFolderDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}

