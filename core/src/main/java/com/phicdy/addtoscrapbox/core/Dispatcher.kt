package com.phicdy.addtoscrapbox.core

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.withContext
import java.util.Collections


class Dispatcher {
    private val stores = Collections.synchronizedList(mutableListOf<Store<*>>())

    fun register(store: Store<*>) {
        stores.add(store)
    }

    fun unregister(store: Store<*>) {
        stores.remove(store)
    }

    suspend fun dispatch(action: Action<*>) {
        for (store in stores) {
            withContext(store.viewModelScope.coroutineContext) {
                store.notify(action)
            }
        }
    }
}