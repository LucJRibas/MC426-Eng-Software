package com.project.lembretio

import com.project.lembretio.utils.isEventTimeValid
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ValidEventsTest {

    private val event: Event = Event(
        name = "Medication Event",
        repeating = true,
        date = LocalDate.now(),
        time = LocalTime.now(),
        times = mutableListOf(),
        alarmId = 1,
        uri = null,
        isMedication = true,
        comment = "Comentario"
    )

    @Test
    fun `event is medication and repeating`() {
        event.isMedication = true
        event.repeating = true
        assert(isEventTimeValid(event))
    }

    @Test
    fun `invalid event that is medication but not repeating`() {
        event.isMedication = true
        event.repeating = false
        event.date = LocalDate.now()
        val now = LocalTime.now()
        event.times = mutableListOf(now.minusMinutes(2), now.plusMinutes(10))

        assert(!isEventTimeValid(event))
    }

    @Test
    fun `invalid event that is not medication`() {
        event.isMedication = false
        event.repeating = false
        event.date = LocalDate.now()
        val now = LocalTime.now()
        event.times = mutableListOf(now.minusMinutes(1))

        assert(!isEventTimeValid(event))
    }

    @Test
    fun `valid event that is medication but repeats`() {
        event.isMedication = true
        event.repeating = true
        event.date = LocalDate.now().plusDays(1)
        val now = LocalTime.now()
        event.times = mutableListOf(now.plusMinutes(3), now.plusMinutes(10))
        assert(!isEventTimeValid(event))
    }

    @Test
    fun `valid event that is not a medication`() {
        event.isMedication = false
        event.repeating = false
        event.date = LocalDate.now().plusDays(1)
        val now = LocalTime.now()
        event.times = mutableListOf(now.plusMinutes(1))
        assert(!isEventTimeValid(event))
    }
}