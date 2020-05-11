package com.phicdy.addtoscrapbox.main

import com.phicdy.addtoscrapbox.core.Action
import com.phicdy.addtoscrapbox.core.Dispatcher
import com.phicdy.addtoscrapbox.core.Store

class MainStore(dispatcher: Dispatcher) :
    Store<MainState>(dispatcher) {

    override suspend fun notify(action: Action<*>) {
        when (action) {
            is MainAction -> _state.value = action.value
        }
    }
}