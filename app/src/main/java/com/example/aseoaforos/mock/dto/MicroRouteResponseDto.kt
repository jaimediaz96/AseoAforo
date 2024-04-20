package com.example.aseoaforos.mock.dto

import com.example.aseoaforos.mock.entity.Client

data class MicroRouteResponseDto(
    val microRouteId: Long,
    val routeName: String,
    val routeDescription: String,
    val microRouteName: String,
    var plate: String,
    val clientList: List<Client>
)
