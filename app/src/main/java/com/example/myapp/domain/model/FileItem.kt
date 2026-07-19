package com.example.myapp.domain.model

data class FileItem(
    val id: String,
    val parentId: String?,
    val name: String,
    val isDirectory: Boolean,
    val date: String,
    val size: Long?,
    val contentType: String?
)