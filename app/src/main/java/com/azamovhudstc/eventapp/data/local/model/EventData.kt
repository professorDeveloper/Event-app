package com.azamovhudstc.eventapp.data.local.model

data class EventData(
    val id: Int,
    val actionIcon: Int,
    val action:String,
    val actionName: Int,
    var actionState: Boolean
)
