package com.example.aseoaforos.mock.entity

import java.util.Date

data class MicroRoute(
    val microRouteId: Long,
    val routeName: String,
    val routeDescription: String,
    val microRouteName: String,
    var plate: String,
    var stateId: Long = 1,
    var truckId: Long? = null,
    var dateFinish: Date? = null,
    var mileage: Long? = null,
    var microRouteCollectTime: Long? = null,
    var microRouteTripHomeTime: Long? = null,
    var totalCollect: Double? = null,
    var density: Double? = null,
    var microRouteSign: String? = null,
    var descriptionAbandon: String? = null
)