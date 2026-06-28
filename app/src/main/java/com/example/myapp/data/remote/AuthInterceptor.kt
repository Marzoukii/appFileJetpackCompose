package com.example.myapp.data.remote

import android.net.Credentials
import kotlinx.coroutines.runBlocking

class AuthInterceptor @Inject constructor(
    private val userPrefs: UserPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials = runBlocking { userPrefs.getCredentials.first() }
        val request = chain.request().newBuilder()
            .header("Authorization", Credentials.basic(credentials.login, credentials.password))
            .build()
        return chain.proceed(request)
    }
}