package com.azamovhudstc.eventapp.usecase

import com.azamovhudstc.eventapp.data.local.model.EventData
import kotlinx.coroutines.flow.Flow

interface EventsUseCase {

    fun allEvents(): Flow<List<EventData>>

    fun changeEventState(eventId: Int, state: Boolean): Flow<List<EventData>>

    fun enableAllEventStates(): Flow<List<EventData>>

    fun disableAllEventStates(): Flow<List<EventData>>

}