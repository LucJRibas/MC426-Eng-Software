package com.project.lembretio

data class Event(
    var name: String,
    val repeating: Boolean,
    var isChecked: Boolean = false,
    var date: String = "aaa"
)