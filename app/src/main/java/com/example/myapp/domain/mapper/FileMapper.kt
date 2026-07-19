package com.example.myapp.data.mapper

import com.example.myapp.data.remote.dto.FileItemDto
import com.example.myapp.data.remote.dto.UserResponseDto
import com.example.myapp.domain.model.FileItem
import com.example.myapp.domain.model.User

fun FileItemDto.toDomain(): FileItem {
    return FileItem(
        id = this.id,
        parentId = this.parentId,
        name = this.name,
        isDirectory = this.isDir,
        date = this.modificationDate,
        size = this.size,
        contentType = this.contentType
    )
}

fun List<FileItemDto>.toDomain(): List<FileItem> = map { it.toDomain() }

fun UserResponseDto.toDomain(): User {
    return User(
        firstName = this.firstName,
        lastName = this.lastName,
        rootItem = this.rootItem.toDomain()
    )
}