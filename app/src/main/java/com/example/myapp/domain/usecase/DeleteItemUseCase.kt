package com.example.myapp.domain.usecase

import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.repository.FileRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: FileRepository
) {

    suspend operator fun invoke(itemId: String): NetworkResult<Unit> =
        repository.deleteItem(itemId)
}