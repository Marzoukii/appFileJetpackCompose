package com.example.myapp.ui.enums

import com.example.myapp.domain.model.User

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}
