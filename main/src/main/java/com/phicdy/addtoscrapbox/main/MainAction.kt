package com.phicdy.addtoscrapbox.main

import com.phicdy.addtoscrapbox.core.Action

data class MainAction(override val value: MainState) : Action<MainState>
