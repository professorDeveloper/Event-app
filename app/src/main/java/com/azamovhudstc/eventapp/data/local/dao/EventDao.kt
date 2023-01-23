package com.azamovhudstc.eventapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azamovhudstc.eventapp.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("select exists (select * from events)")
    suspend fun isAvailableEvents(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInitialEvents(events: List<EventEntity>)

    @Query("select * from events")
    suspend fun getAllEvents(): List<EventEntity>

    @Query("update events set actionState=:state where id=:eventId")
    suspend fun changeEventState(eventId: Int, state: Boolean)

    @Query("update events set actionState=1")
    suspend fun enableAllEventStates()

    @Query("update events set actionState=0")
    suspend fun disableAllEventStates()

}