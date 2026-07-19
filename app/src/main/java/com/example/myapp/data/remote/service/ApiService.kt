package com.example.myapp.data.remote.service

import com.example.myapp.data.remote.dto.FileItemDto
import com.example.myapp.data.remote.dto.UserResponseDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("me")
    suspend fun getCurrentUser(): UserResponseDto

    @GET("items/{id}")
    suspend fun getFolderContent(@Path("id") folderId: String): List<FileItemDto>

    @POST("items/{id}")
    @Headers("Content-Type: application/json")
    suspend fun createFolder(
        @Path("id") parentId: String,
        @Body body: Map<String, String>
    ): FileItemDto

    @POST("items/{id}")
    suspend fun uploadFile(
        @Path("id") parentId: String,
        @Header("Content-Disposition") contentDisposition: String,
        @Header("Content-Type") contentType: String = "application/octet-stream",
        @Body fileBody: RequestBody
    ): FileItemDto

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") itemId: String): Response<Unit>

    @GET("items/{id}/data")
    @Streaming
    suspend fun downloadItem(@Path("id") itemId: String): ResponseBody
}