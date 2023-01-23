package com.azamovhudstc.eventapp.repository

import com.azamovhudstc.eventapp.data.local.entity.EventEntity

interface EventRepository {
    suspend fun allEvents(): List<EventEntity>

    suspend fun changeEventState(eventId: Int, state: Boolean)

    suspend fun enableAllEventStates()

    suspend fun disableAllEventStates()

}