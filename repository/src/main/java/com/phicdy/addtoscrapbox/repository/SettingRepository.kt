package com.phicdy.addtoscrapbox.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingRepository(private val context: Context) {

    suspend fun store(projectUrl: String) {
        withContext(Dispatchers.IO) {
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                .putString(
                    KEY_PROJECT_URL, projectUrl
                ).apply()
        }
    }

    suspend fun getProjectUrl(): String {
        return withContext(Dispatchers.IO) {
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(KEY_PROJECT_URL, "") ?: ""
        }
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "pref"
        private const val KEY_PROJECT_URL = "keyProjectUrl"
    }
}