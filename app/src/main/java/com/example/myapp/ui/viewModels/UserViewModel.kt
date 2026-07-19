package com.example.myapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.NetworkResult
import com.example.myapp.domain.model.User
import com.example.myapp.domain.usecase.GetCurrentUserUseCase
import com.example.myapp.ui.enums.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            when (val result = getCurrentUserUseCase()) {
                is NetworkResult.Success -> {
                    _userState.value = UserState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _userState.value = UserState.Error(result.exception.message ?: "Failed to fetch user")
                }
            }
        }
    }
}
