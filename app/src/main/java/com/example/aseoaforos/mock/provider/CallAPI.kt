package com.example.aseoaforos.mock.provider

import android.content.Context
import android.widget.Toast
import com.example.aseoaforos.R
import com.example.aseoaforos.mock.dto.MicroRouteResponseDto
import com.example.aseoaforos.mock.entity.Client
import com.example.aseoaforos.mock.entity.Incident
import com.example.aseoaforos.mock.entity.MicroRoute
import com.example.aseoaforos.mock.entity.Provision
import com.example.aseoaforos.mock.entity.Refuel
import com.example.aseoaforos.mock.entity.TempClient
import com.example.aseoaforos.mock.entity.Truck
import com.example.aseoaforos.mock.entity.User

object CallAPI {
    // CALL API CLIENT AUT
    fun authentication(username: String, password: String): User? {
        if (username.equals("felra", true) && password.equals("123", true)) {
            return User(4, "Felipe Carlos Ramírez Torres")
        } else if (username.equals("jaime", true) && password.equals("123", true)) {
            return User(13, "JAIME EDUARDO DIAZ TOBON")
        }
        return null
    }

    // CALL API CLIENT FORGET PASSWORD
    fun forgetPassword(context: Context, email: String): Boolean {
        return if (!email.equals("felra@mail.com", true)) {
            Toast.makeText(
                context,
                context.getString(R.string.errorEmailDB),
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.changePassword),
                Toast.LENGTH_LONG
            ).show()
            true
        }
    }

    // CALL API CLIENT ADD TEMP CLIENT
    fun addTempClient(context: Context, tempClient: TempClient?): Long? {
        return if (tempClient != null) {
            seqId += 1L
            seqId
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddTempClient),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API CLIENT ADD PHOTOS
    fun addPhotosTempClient(context: Context, tempClientId: Long, photos: List<String>): Boolean {
        return if (photos.isNotEmpty() && tempClientId > 0L) {
            true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddPhoto),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    // CALL API MICRO ROUTE BUSINESS GET
    fun getMicroRoute(context: Context, userId: Long): MicroRouteResponseDto? {
        return if (userId == 4L) {
            MicroRouteResponseDto(
                1,
                "AZUL",
                "FUNZA",
                "12345",
                "ABC544",
                listOf(
                    Client(
                        1,
                        "METALURGIA INDUSTRIAL S.A.",
                        "AUT. MEDELLÍN KM 1.5, BODEGA 6, PARQUE EMPRESARIAL SAN FERNANDO",
                        1
                    ),
                    Client(
                        2,
                        "ENERGÍA INDUSTRIAL RENOVABLE LTDA.",
                        "AUT MEDELLIN KIM 1,5 BOD. 6 PARQ EMPRE SAN BERNANDO",
                        1
                    ),
                    Client(
                        3,
                        "INGENIERÍA Y PROCESOS INDUSTRIALES LTDA.",
                        "CARRERA 2 # 3-45, BODEGA 10, PARQUE INDUSTRIAL EL PROGRESO",
                        1
                    ),
                    Client(
                        4,
                        "TECNOLOGÍA INDUSTRIAL AVANZADA S.A.",
                        "AVE, SEC IND MEDELLÍN, BOD 12, PARQUE DE NEGOCIOS LA ESPERANZA",
                        2
                    ),
                    Client(
                        5,
                        "FABRICACIÓN INDUSTRIAL GLOBAL INC.",
                        "CALLE 5 # 9-87, OFICINA 3, PARQUE DE TECNOLOGÍA LOS PINOS",
                        1
                    ),
                    Client(
                        6,
                        "MECÁNICA INDUSTRIAL INNOVADORA S.A.",
                        "AUTOPISTA MEDELLÍN KM 1.5, BODEGA 6, PARQUE LOGÍSTICO EL ROSAL",
                        3
                    )
                )
            )
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorGetMicroRoute),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API MICRO ROUTE BUSINESS ADD CREW
    fun addCrew(context: Context, crewIdList: List<Long>): Boolean {
        return if (crewIdList.isNotEmpty()) {
            true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddCrew),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    // CALL API MICRO ROUTE BUSINESS UPDATE STATE
    fun updateStateMicroRoute(context: Context, microRouteId: Long, stateId: Long): Boolean {
        return if (microRouteId == 1L) {
            when (stateId) {
                2L -> true
                3L -> true
                4L -> true
                5L -> true
                6L -> true
                7L -> true
                8L -> true
                9L -> true
                10L -> true
                else -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.errorUpdateState),
                        Toast.LENGTH_LONG
                    ).show()
                    false
                }
            }
        } else false
    }

    // CALL API MICRO ROUTE BUSINESS UPDATE TRUCK ID
    fun updateMicroRouteTruckId(context: Context, truckId: Long): Boolean {
        return when (truckId) {
            1L -> true
            2L -> true
            else -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.errorUpdateTruckId),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
    }

    // CALL API MICRO ROUTE BUSINESS UPDATE
    fun updateMicroRoute(context: Context, microRoute: MicroRoute?): Boolean {
        return if (microRoute != null) {
            true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorUpdateMicroRoute),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }


    // CALL API TRUCK GET BY PLATE
    fun getByPlate(context: Context, plate: String): Long? {
        return if (plate.equals("ABC544", true)) {
            return 1L
        } else if (plate.equals("ABC123", true)) {
            return 2L
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorPlateDB),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API TRUCK UPDATE
    fun updateTruck(context: Context, truck: Truck): Boolean {
        return when (truck.truckId) {
            1L -> true
            2L -> true
            else -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.errorUpdateTruck),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
    }

    // CALL API TRUCK GET CAPACITY BY TRUCK ID
    fun getTruckCapacity(context: Context, truckId: Long): Double? {
        return if (truckId == 1L) 11.0
        else {
            Toast.makeText(
                context,
                context.getString(R.string.errorGetCapacityTruck),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API TRUCK UPDATE MILEAGE
    fun updateTruckKm(context: Context, mileage: Long): Boolean {
        return if (mileage > 0) true
        else {
            Toast.makeText(
                context,
                context.getString(R.string.errorUpdateKmFinishTruck),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    // CALL API REFUEL CREATE
    fun createRefuel(context: Context, refuel: Refuel?): Long? {
        return if (refuel != null) {
            seqId += 1L
            seqId
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddRefuel),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API REFUEL ADD PHOTOS
    fun addPhotosRefuel(context: Context, refuelId: Long, photos: List<String>): Boolean {
        return if (photos.isNotEmpty() && refuelId > 0L) {
            true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddPhoto),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    // CALL API INCIDENT CREATE
    fun createIncident(context: Context, incident: Incident?): Long? {
        return if (incident != null) {
            seqId += 1L
            seqId
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddIncident),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API INCIDENT ADD PHOTOS
    fun addPhotosIncident(context: Context, incidentId: Long, photos: List<String>): Boolean {
        return if (photos.isNotEmpty() && incidentId > 0L) {
            true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddPhoto),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    // CALL API PROVISION CREATE
    fun createProvision(context: Context, provision: Provision?): Long? {
        return if (provision != null) {
            seqId += 1L
            seqId
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddProvision),
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    // CALL API PROVISION ADD PHOTOS
    fun addPhotosProvision(context: Context, provisionId: Long, photos: List<String>): Boolean {
        return if (photos.isNotEmpty() && provisionId > 0L) {
            true
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddPhoto),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    // CALL API PROVISION CREATE TOLL
    fun createToll(context: Context, names: List<String>): List<Long> {
        return if (names.isNotEmpty()) {
            names.map {
                seqId += 1L
                seqId
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.errorAddProvision),
                Toast.LENGTH_LONG
            ).show()
            listOf()
        }
    }

    private var seqId: Long = 1L
}