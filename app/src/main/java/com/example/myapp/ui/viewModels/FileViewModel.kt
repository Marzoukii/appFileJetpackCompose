package com.example.myapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.domain.model.FileItem
import com.example.myapp.domain.usecase.*
import com.example.myapp.ui.enums.FileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val getFilesUseCase: GetFilesUseCase,
    private val getRootUseCase: GetRootUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FileUiState>(FileUiState.Loading)
    val uiState: StateFlow<FileUiState> = _uiState.asStateFlow()

    private var currentFolderId: String? = null

    init {
        loadRoot()
    }

    fun loadRoot() {
        viewModelScope.launch {
            _uiState.value = FileUiState.Loading
            when (val result = getRootUseCase()) {
                is NetworkResult.Success -> {
                    currentFolderId = result.data
                    loadFiles(result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = FileUiState.Error(result.exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun loadFiles(folderId: String) {
        viewModelScope.launch {
            _uiState.value = FileUiState.Loading
            currentFolderId = folderId
            when (val result = getFilesUseCase(folderId)) {
                is NetworkResult.Success -> {
                    _uiState.value = FileUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = FileUiState.Error(result.exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun createFolder(name: String) {
        val folderId = currentFolderId ?: return
        viewModelScope.launch {
            when (val result = createFolderUseCase(folderId, name)) {
                is NetworkResult.Success -> {
                    // Refresh current folder
                    loadFiles(folderId)
                }
                is NetworkResult.Error -> {
                    // Handle error (could use a side effect or toast)
                }
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            when (val result = deleteItemUseCase(itemId)) {
                is NetworkResult.Success -> {
                    currentFolderId?.let { loadFiles(it) }
                }
                is NetworkResult.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun uploadFile(name: String, bytes: ByteArray) {
        val folderId = currentFolderId ?: return
        viewModelScope.launch {
            when (val result = uploadFileUseCase(folderId, name, bytes)) {
                is NetworkResult.Success -> {
                    loadFiles(folderId)
                }
                is NetworkResult.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun navigateBack() {
        // Implementation for back navigation if folder structure is handled here
        // or just rely on NavController in UI
    }
}
