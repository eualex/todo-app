package com.example.todoapp.presentation.screen.settings

import com.example.todoapp.R

data class SettingSectionItem(
    val icon: Int,
    val name: String,
    val color: Int? = null
)

sealed class SettingSections(val items: List<SettingSectionItem>) {
    data class Account(val itemList: List<SettingSectionItem>): SettingSections(itemList)
    data class Preferences(val itemList: List<SettingSectionItem>): SettingSections(itemList)
    data class More(val itemList: List<SettingSectionItem>): SettingSections(itemList)
}

