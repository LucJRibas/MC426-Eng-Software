package com.project.lembretio

data class Event(
    val name: String,
    val repeating: Boolean,
    var isChecked: Boolean = false
)