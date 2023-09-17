package com.project.lembretio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository): ViewModel()
{
    val events: LiveData<List<Event>> = repository.allEvents.asLiveData()

    fun addEvent(event: Event) = viewModelScope.launch {
        repository.insertEvent(event)
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        repository.updateEvent(event)
    }

}

class EventModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(EventViewModel::class.java))
            return EventViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}