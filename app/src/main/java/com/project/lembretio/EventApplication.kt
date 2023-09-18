package com.project.lembretio

import android.app.Application

class EventApplication: Application() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { EventRepository(database.eventDao()) }

}