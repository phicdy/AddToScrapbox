package com.phicdy.addtoscrapbox.main

import android.net.Uri
import com.phicdy.addtoscrapbox.core.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActionCreator(
    private val dispatcher: Dispatcher
) {
    suspend fun execute(projectUrl: String, input: String) {
        withContext(Dispatchers.IO) {
            val uri = Uri.parse("$projectUrl$input")
            dispatcher.dispatch(MainAction(MainState.Loaded(uri)))
        }
    }
}