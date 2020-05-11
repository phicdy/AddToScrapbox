package com.phicdy.addtoscrapbox.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope

abstract class Store<T>(
    protected val dispatcher: Dispatcher
) : ViewModel() {

    init {
        dispatcher.register(this)
    }

    protected val _state = MutableLiveData<T>()
    val state: LiveData<T>
        get() = _state

    abstract suspend fun notify(action: Action<*>)

    override fun onCleared() {
        dispatcher.unregister(this)
        super.onCleared()
    }
}
