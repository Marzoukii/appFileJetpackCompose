package com.example.myapp.domain.usecase

import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.repository.FileRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val repository: FileRepository
) {

    suspend operator fun invoke(itemId: String): NetworkResult<ResponseBody> =
        repository.downloadItem(itemId)
}