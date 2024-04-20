package com.example.aseoaforos.mock.entity

data class TempClient(
    var tempClientId: Long? = null,
    val companyName: String,
    val nit: String,
    val phone: String,
    val address: String,
    val email: String,
    val contactName: String,
    val contactPhone: String,
    val collectTypeId: Long,
    val photoList: MutableList<String> = mutableListOf()
)
