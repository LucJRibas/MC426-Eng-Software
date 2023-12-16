package com.project.lembretio.utils

import android.widget.Toast
import com.project.lembretio.Event
import java.time.LocalDateTime

fun isEventTimeValid(event: Event): Boolean {
    if (!event.isMedication) {
        val dateTime = LocalDateTime.of(event.date, event.times[0])
        if (dateTime.isBefore(LocalDateTime.now())) {
            return false
        }
    }
    if (event.isMedication && !event.repeating) {
        if (event.times.any { LocalDateTime.of(event.date, it).isBefore(LocalDateTime.now()) }) {
            return false
        }
    }
    return true
}

fun isEventNameValid(event: Event): Boolean {
    return !(event.name == null || event.name == "")
}