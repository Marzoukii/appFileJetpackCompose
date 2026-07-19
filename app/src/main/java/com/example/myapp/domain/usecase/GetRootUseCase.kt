package com.example.myapp.domain.usecase

import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.repository.FileRepository
import javax.inject.Inject

class GetRootUseCase @Inject constructor(
    private val repository: FileRepository
) {

    suspend operator fun invoke(): NetworkResult<String> =
        repository.getRootFolderId()
}