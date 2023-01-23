package com.azamovhudstc.eventapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.azamovhudstc.eventapp.data.local.model.EventData

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val actionIcon: Int,
    @ColumnInfo(name = "action")
    val action: String,
    val actionName: Int,
    val actionState: Boolean = false
) {
    fun eventEntityToEventData(): EventData = EventData(
        id = id,
        actionIcon = actionIcon,
        action = action,
        actionName = actionName,
        actionState = actionState
    )
}
