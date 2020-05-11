package com.phicdy.addtoscrapbox.core

interface Action<out T> {
    val value: T
}