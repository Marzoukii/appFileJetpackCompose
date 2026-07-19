package com.example.myapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_LOGIN    = stringPreferencesKey("login")
        private val KEY_PASSWORD = stringPreferencesKey("password")
    }

    val credentials: Flow<Pair<String, String>> = context.dataStore.data.map { prefs ->
        Pair(
            prefs[KEY_LOGIN]    ?: "",
            prefs[KEY_PASSWORD] ?: ""
        )
    }

    suspend fun saveCredentials(login: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGIN]    = login
            prefs[KEY_PASSWORD] = password
        }
    }

    suspend fun clearCredentials() {
        context.dataStore.edit { it.clear() }
    }
}