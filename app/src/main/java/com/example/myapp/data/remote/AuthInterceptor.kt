package com.example.myapp.data.remote

import com.example.myapp.data.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPrefs: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val (login, password) = runBlocking { userPrefs.credentials.first() }
        val request = chain.request().newBuilder()
            .header("Authorization", Credentials.basic(login, password))
            .build()
        return chain.proceed(request)
    }
}