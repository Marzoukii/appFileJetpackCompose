package com.example.myapp.domain.usecase

import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.repository.FileRepository
import com.example.myapp.domain.model.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: FileRepository
) {

    suspend operator fun invoke(): NetworkResult<User> =
        repository.getCurrentUser()
}