package com.example.aseoaforos.mock.entity

import java.util.Date

data class Collect(
    val collectTypeId: Long,
    var totalCollected: Double = 0.0,
    var isCollected: Boolean = false,
    var collectSign: String = "",
    var collectDate: Date,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    val microRouteId: Long,
    val clientId: Long? = null,
    val tempClientId: Long? = null,
    var noCollectedId: Long = 0,
    val recipientList: MutableList<Recipient> = mutableListOf(),
    val photoList: MutableList<String> = mutableListOf(),
)
