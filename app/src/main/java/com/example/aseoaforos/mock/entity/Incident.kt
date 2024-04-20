package com.example.aseoaforos.mock.entity

import java.util.Date

data class Incident(
    val description: String,
    val incidentDate: Date,
    val incidentTypeId: Long,
    val microRouteId: Long,
    val place: String,
    val photos: MutableList<String> = mutableListOf(),
    var incidentTime: Long = 0,
)