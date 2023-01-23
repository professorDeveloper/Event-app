package com.azamovhudstc.eventapp.usecase.impl

import com.azamovhudstc.eventapp.data.local.model.EventData
import com.azamovhudstc.eventapp.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.azamovhudstc.eventapp.usecase.EventsUseCase
import javax.inject.Inject

class EventsUseCaseImpl
@Inject constructor(
    private val repository: EventRepository
) : EventsUseCase {

    override fun allEvents() = flow<List<EventData>> {
        val result = repository.allEvents().map { it.eventEntityToEventData() }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun changeEventState(eventId: Int, state: Boolean) = flow<List<EventData>> {
        repository.changeEventState(eventId, state)
        val result = repository.allEvents().map { it.eventEntityToEventData() }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun enableAllEventStates() = flow<List<EventData>> {
        repository.enableAllEventStates()
        val result = repository.allEvents().map { it.eventEntityToEventData() }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun disableAllEventStates() = flow<List<EventData>> {
        repository.disableAllEventStates()
        val result = repository.allEvents().map { it.eventEntityToEventData() }
        emit(result)
    }.flowOn(Dispatchers.IO)

}