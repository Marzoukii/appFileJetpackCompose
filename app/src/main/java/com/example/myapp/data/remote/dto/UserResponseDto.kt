package com.example.myapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName")  val lastName: String,
    @SerializedName("rootItem")  val rootItem: FileItemDto
)