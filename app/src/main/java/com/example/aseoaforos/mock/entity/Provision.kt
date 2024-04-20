package com.example.aseoaforos.mock.entity

import java.util.Date

data class Provision(
    val kmStart: Long,
    val kmFinish: Long,
    val provisionDate: Date,
    val weight: Double,
    val microRouteId: Long,
    val provisionPlaceId: Long,
    val provisionTypeId: Long,
    val tollList: List<Long>,
    val photos: MutableList<String> = mutableListOf(),
    val provisionTimeTrip: Long,
    val provisionTimePlace: Long,
)