package com.example.aseoaforos.mock.entity

import java.util.Date

data class Refuel(
    val place: String,
    val quantity: Double,
    val cost: Double,
    val refuelDate: Date,
    val microRouteId: Long,
    val photos: MutableList<String> = mutableListOf(),
    var refuelTime: Long = 0L,
    var refuelId: Long = 0L
)