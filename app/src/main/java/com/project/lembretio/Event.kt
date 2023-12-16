package com.project.lembretio

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Entity(tableName = "event_table")
@Parcelize
data class Event(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "repeating") var repeating: Boolean,
    @ColumnInfo(name = "date") var date: LocalDate,
    @ColumnInfo(name = "time_created") var time: LocalTime,
    @ColumnInfo(name = "times") var times: MutableList<LocalTime>,
    @ColumnInfo(name = "alarm_id") var alarmId: Int,
    @ColumnInfo(name = "uri") var uri: Uri?,
    @ColumnInfo(name = "is_med") var isMedication: Boolean,
    @ColumnInfo(name = "comments") var comment: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable {

    val createdDateFormatted : String
        get() =date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    fun setDateFromString(stringDate: String) {
        date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun countDoses() : Long{

        val today = LocalDate.now()
        val dosesPerDay = times.size
        var dosesTaken = 0L
        val currentTime = LocalTime.now()

        //para cada dose do primeiro dia eu verifico se
        //o tempo de dose veio depois do evento criado
        // se veio, eu verifico se o tempo atual eh depois do tempo da dose.
        // Se  tempoCriacaoEvento < tempoDose[i] < tempoatual, entao add uma Dose

        if (today.isBefore(date)){
            return 0
        }


        for (doseTime in times) {
            if (doseTime.isAfter(time)) {
                if (currentTime.isAfter(doseTime)) {
                    dosesTaken++
                }
            }
        }


        val daysPassed = date.plusDays(1).until(today,ChronoUnit.DAYS)

        if (daysPassed == -1L){
            return dosesTaken
        }

        dosesTaken += daysPassed * dosesPerDay


        for (doseTime in times) {
            if (currentTime.isAfter(doseTime)) {
                dosesTaken++
            }
        }

        return dosesTaken
    }

}