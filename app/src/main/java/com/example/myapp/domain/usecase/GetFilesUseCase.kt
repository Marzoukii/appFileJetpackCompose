package com.example.myapp.domain.usecase

import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.repository.FileRepository
import com.example.myapp.domain.model.FileItem
import javax.inject.Inject

class GetFilesUseCase @Inject constructor(
    private val repository: FileRepository
) {

    suspend operator fun invoke(folderId: String): NetworkResult<List<FileItem>> =
        repository.getFolderContent(folderId)
}