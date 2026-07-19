package com.example.myapp.data.repository

import com.example.myapp.data.mapper.toDomain
import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.data.remote.service.ApiService
import com.example.myapp.domain.model.FileItem
import com.example.myapp.domain.model.User
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getCurrentUser(): NetworkResult<User> {
        return try {
            NetworkResult.Success(api.getCurrentUser().toDomain())
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getRootFolderId(): NetworkResult<String> {
        return try {
            NetworkResult.Success(api.getCurrentUser().rootItem.id)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getFolderContent(folderId: String): NetworkResult<List<FileItem>> {
        return try {
            NetworkResult.Success(api.getFolderContent(folderId).toDomain())
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun createFolder(parentId: String, name: String): NetworkResult<FileItem> {
        return try {
            val response = api.createFolder(parentId, mapOf("name" to name))
            NetworkResult.Success(response.toDomain())
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun deleteItem(itemId: String): NetworkResult<Unit> {
        return try {
            val response = api.deleteItem(itemId)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(Exception("Failed to delete: ${response.code()}"))
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun uploadFile(parentId: String, name: String, bytes: ByteArray): NetworkResult<FileItem> {
        return try {
            val disposition = "attachment;filename*=utf-8''$name"
            val requestBody = bytes.toRequestBody("application/octet-stream".toMediaType())

            val response = api.uploadFile(
                parentId = parentId,
                contentDisposition = disposition,
                fileBody = requestBody
            )
            NetworkResult.Success(response.toDomain())
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun downloadItem(itemId: String): NetworkResult<ResponseBody> {
        return try {
            NetworkResult.Success(api.downloadItem(itemId))
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}