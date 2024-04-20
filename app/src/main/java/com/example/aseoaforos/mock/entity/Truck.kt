package com.example.aseoaforos.mock.entity

data class Truck(
    val truckId: Long,
    val plate: String,
    val papers: Boolean,
    var mileage: Long,
    val tire: String,
    val pressure: String,
    val fuel: String,
    val light: String,
    val honk: String,
    val brake: String,
    val oil: String,
    val observation: String,
    val capacity: Double,
)