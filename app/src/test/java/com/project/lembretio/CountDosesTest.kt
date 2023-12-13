package com.project.lembretio

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CountDosesTest {
    val hour_min_formmater = DateTimeFormatter.ofPattern("HH:mm")
    private fun createEvent(date: LocalDateTime, times: List<String>): Event {
        val parsedTimes = times.map { LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm")) }
        return Event(
            name = "Medication Event",
            repeating = true,
            date = date.toLocalDate(),
            time = date.toLocalTime(),
            times = parsedTimes.toMutableList(),
            alarmId = 1,
            uri = null,
            isMedication = true
        )
    }
    @Test
    fun `event 1 min before should return 1`() {
        val currentDateTime = LocalDateTime.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDateTime.minusMinutes(2), listOf(oneMinuteBefore))
        assertEquals(1, event.countDoses())
    }

    @Test
    fun `event 1 min after should return 0`() {
        val currentDateTime = LocalDateTime.now()
        val oneMinuteAfter = hour_min_formmater.format(currentDateTime.plus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDateTime, listOf(oneMinuteAfter))
        assertEquals(0, event.countDoses())
    }

    @Test
    fun `event 1 day before count`() {
        val currentDateTime = LocalDateTime.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDateTime.minusDays(1).minusMinutes(2), listOf(oneMinuteBefore))
        assertEquals(2, event.countDoses())
    }

    @Test
    fun `event various days before count`() {
        val currentDateTime = LocalDateTime.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDateTime.minusDays(5).minusMinutes(2), listOf(oneMinuteBefore))
        assertEquals(6, event.countDoses())
    }

    @Test
    fun `event multiple times`() {
        val currentDateTime = LocalDateTime.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val twoMinutesBefore = hour_min_formmater.format(currentDateTime.minus(2, ChronoUnit.MINUTES))
        val event = createEvent(currentDateTime.minusDays(1).minusMinutes(3), listOf(oneMinuteBefore, twoMinutesBefore))
        assertEquals(4, event.countDoses())
    }
}