package com.phicdy.addtoscrapbox.main

import android.net.Uri
import android.util.Log
import com.phicdy.addtoscrapbox.core.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class MainActionCreator(
    private val dispatcher: Dispatcher
) {
    suspend fun execute(projectUrl: String, input: String) {
        withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(input).get()
                val result = doc.title()
                val uri = Uri.parse("$projectUrl$result?body=$input")
                dispatcher.dispatch(MainAction(MainState.Loaded(uri)))
            } catch (e: IOException) {
            }
        }
    }
}