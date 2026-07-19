package com.example.myapp.domain.usecase

import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.repository.FileRepository
import com.example.myapp.domain.model.FileItem
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val repository: FileRepository
) {

    suspend operator fun invoke(parentId: String, name: String, bytes: ByteArray): NetworkResult<FileItem> =
        repository.uploadFile(parentId, name, bytes)
}