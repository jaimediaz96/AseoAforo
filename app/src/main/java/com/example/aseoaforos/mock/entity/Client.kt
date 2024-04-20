package com.example.aseoaforos.mock.entity

import com.example.aseoaforos.util.CollectionState

data class Client(
    val clientId: Long,
    val companyName: String,
    val address: String,
    val collectTypeId: Long,
    var state: CollectionState = CollectionState.UNDEFINED,
    val tempClientId: Long? = null,
)