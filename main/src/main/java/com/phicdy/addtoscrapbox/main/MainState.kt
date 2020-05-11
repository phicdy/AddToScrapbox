package com.phicdy.addtoscrapbox.main

import android.net.Uri

sealed class MainState {
    object Loading: MainState()
    data class Loaded(val uri: Uri): MainState()
}