package com.example.multiplicationgame

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    var items = mutableStateListOf<UiItem>()
    var shuffledItems = mutableStateListOf<UiItem>()
    var addedItems = mutableStateListOf<UiItem>()
}

data class UiItem(
    val num1: Int,
    val num2: Int,
    val answer: Int,
    var statusAnswer: Int,
)