package com.project.lembretio

import com.project.lembretio.utils.countDoses
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CountDosesTest {
    val hour_min_formmater = DateTimeFormatter.ofPattern("HH:mm")
    private fun createEvent(date: LocalDate, times: List<String>): Event {
        val parsedTimes = times.map { LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm")) }
        return Event(
            name = "Medication Event",
            repeating = true,
            date = date,
            times = parsedTimes.toMutableList(),
            alarmId = 1,
            uri = null,
            isMedication = true
        )
    }
    @Test
    fun `event 1 min before should return 1`() {
        val currentDateTime = LocalDateTime.now()
        val currentDate = LocalDate.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDate, listOf(oneMinuteBefore))
        assertEquals(1, countDoses(event))
    }

    @Test
    fun `event 1 min after should return 0`() {
        val currentDateTime = LocalDateTime.now()
        val currentDate = LocalDate.now()
        val oneMinuteAfter = hour_min_formmater.format(currentDateTime.plus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDate, listOf(oneMinuteAfter))
        assertEquals(0, countDoses(event))
    }

    @Test
    fun `event 1 day before count`() {
        val currentDateTime = LocalDateTime.now()
        val currentDate = LocalDate.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDate.minusDays(1), listOf(oneMinuteBefore))
        assertEquals(2, countDoses(event))
    }

    @Test
    fun `event various days before count`() {
        val currentDateTime = LocalDateTime.now()
        val currentDate = LocalDate.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val event = createEvent(currentDate.minusDays(5), listOf(oneMinuteBefore))
        assertEquals(6, countDoses(event))
    }

    @Test
    fun `event multiple times`() {
        val currentDateTime = LocalDateTime.now()
        val currentDate = LocalDate.now()
        val oneMinuteBefore = hour_min_formmater.format(currentDateTime.minus(1, ChronoUnit.MINUTES))
        val twoMinutesBefore = hour_min_formmater.format(currentDateTime.minus(2, ChronoUnit.MINUTES))
        val event = createEvent(currentDate.minusDays(1), listOf(oneMinuteBefore, twoMinutesBefore))
        assertEquals(4, countDoses(event))
    }
}