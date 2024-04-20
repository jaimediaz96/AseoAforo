package com.example.aseoaforos.mock.provider

import com.example.aseoaforos.mock.entity.CollectType
import com.example.aseoaforos.mock.entity.Crew
import com.example.aseoaforos.mock.entity.FillPercentage
import com.example.aseoaforos.mock.entity.IncidentType
import com.example.aseoaforos.mock.entity.NotCollected
import com.example.aseoaforos.mock.entity.ProvisionPlace
import com.example.aseoaforos.mock.entity.ProvisionType
import com.example.aseoaforos.mock.entity.RecipientType
import com.example.aseoaforos.mock.entity.StateMicroRoute
import com.example.aseoaforos.mock.entity.Toll

object CallAPIList {
    // CALL API MICRO ROUTE BUSINESS GET SATE
    fun getStateMicroRouteList(): List<StateMicroRoute> {
        return listOf(
            StateMicroRoute(1, "CREADA"),
            StateMicroRoute(2, "INICIADA"),
            StateMicroRoute(3, "FINALIZADA"),
            StateMicroRoute(4, "FINALIZADA CON PENDIENTES"),
            StateMicroRoute(5, "REPOSTAJE"),
            StateMicroRoute(6, "INCIDENTE"),
            StateMicroRoute(7, "DISPOSICIÓN"),
            StateMicroRoute(8, "25% completado"),
            StateMicroRoute(9, "50% completado"),
            StateMicroRoute(10, "75% completado"),
            StateMicroRoute(11, "REGRESANDO A BASE"),
        )
    }

    // CALL API MICRO ROUTE BUSINESS GET CREW
    fun getCrewList(): List<Crew> {
        return listOf(
            Crew(1, "Andrés Santiago Jiménez Ramírez"),
            Crew(2, "Santiago Andrés Gómez González"),
            Crew(3, "Laura Daniela Torres Moreno"),
            Crew(4, "Felipe Carlos Ramírez Torres"),
            Crew(5, "Carlos Andrés González Moreno"),
            Crew(6, "Daniela Laura Moreno Jimínez")
        )
    }

    // CALL API PROVISION GET TYPES
    fun getProvisionTypeList(): List<ProvisionType> {
        return listOf(
            ProvisionType(1, "ORGÁNICOS"),
            ProvisionType(2, "RECICLABLES"),
            ProvisionType(3, "ESPECIALES"),
            ProvisionType(4, "PELIGROSOS"),
        )
    }

    // CALL API PROVISION GET PLACES
    fun getProvisionPlaceList(): List<ProvisionPlace> {
        return listOf(
            ProvisionPlace(1, "MONDOÑEDO"),
        )
    }

    // CALL API PROVISION GET TOLLS
    fun getTollList(): List<Toll> {
        return listOf(
            Toll(1, "5364"),
        )
    }

    // CALL API INCIDENT GET TYPE
    fun getIncidentTypeList(): List<IncidentType> {
        return listOf(
            IncidentType(1, "Accidentes de tráfico"),
            IncidentType(2, "Averías mecánicas"),
            IncidentType(3, "Incendios en la carga"),
            IncidentType(4, "Derrames de residuos"),
            IncidentType(5, "Retrasos por condiciones climáticas adversas"),
            IncidentType(6, "Conflictos con la comunidad local"),
            IncidentType(7, "Robos o vandalismo"),
            IncidentType(8, "Problemas de salud para los trabajadores"),
            IncidentType(9, "Infracciones de tránsito"),
            IncidentType(10, "Incumplimiento de normativas ambientales"),
            IncidentType(11, "Otro"),
        )
    }

    // CALL API COLLECT GET NOT COLLECTED
    fun getNotCollectedList(): List<NotCollected> {
        return listOf(
            NotCollected(1, 10001, "Recolección sin inconveniente"),
            NotCollected(2, 10002, "Cerrado"),
            NotCollected(3, 10003, "No permite la recolección"),
            NotCollected(4, 10004, "Imposibilidad de acceso"),
            NotCollected(5, 10005, "Incidente técnico"),
            NotCollected(6, 10006, "Otro"),
        )
    }

    // CALL API COLLECT GET FILL PERCENTAGE
    fun getFillPercentageList(): List<FillPercentage> {
        return listOf(
            FillPercentage(1, 1.0),
            FillPercentage(2, 0.75),
            FillPercentage(3, 0.5),
            FillPercentage(4, 0.25),
        )
    }

    // CALL API COLLECT GET TYPES
    fun getCollectTypeList(): List<CollectType> {
        return listOf(
            CollectType(1, "VOLUMEN"),
            CollectType(2, "ESPECIAL"),
            CollectType(3, "PESO"),
        )
    }

    // CALL API COLLECT GET RECIPIENT TYPE
    fun getRecipientTypeList(): List<RecipientType> {
        return listOf(
            RecipientType(1, "BOLSA DOMÉSTICAS (50X75cm)", 0.031, 1),
            RecipientType(2, "BOLSA SEMI-INDUSTRIAL (60X86cm)", 0.050, 1),
            RecipientType(3, "BOLSA INDUSTRIAL (70X120cm)", 0.111, 1),
            RecipientType(4, "CANECA 20 gal", 0.08, 1),
            RecipientType(5, "CANECA 25 gal", 0.10, 1),
            RecipientType(6, "CANECA 35 gal", 0.13, 1),
            RecipientType(7, "CANECA 55 gal", 0.21, 1),
            RecipientType(8, "CAJA ESTACIONARIA 2 yd3", 1.53, 1),
            RecipientType(9, "CAJA ESTACIONARIA 2.5 yd3", 1.91, 1),
            RecipientType(10, "CAJA ESTACIONARIA 3 yd3", 2.29, 1),
            RecipientType(11, "CAJA ESTACIONARIA 4 yd3", 3.06, 1),
            RecipientType(12, "CAJA ESTACIONARIA 6 yd3", 4.59, 1),
            RecipientType(13, "CAJA ESTACIONARIA 10 yd3", 7.64, 1),
            RecipientType(14, "CAJA ESTACIONARIA 15 yd3", 11.47, 1),
            RecipientType(15, "CAJA ESTACIONARIA 20 yd3", 15.30, 1),
            RecipientType(16, "CAJA ESTACIONARIA 40 yd3", 30.6, 1),
            RecipientType(17, "RECOLECCIÓN DE ESCOMBROS", 1.0, 2),
            RecipientType(18, "RECOLECCIÓN DE PODA", 1.0, 2),
            RecipientType(19, "RECOLECCIÓN RESIDUOS ESPECIALES", 1.0, 2),
            RecipientType(20, "RECOLECCIÓN RESIDUOS PELIGROSOS", 1.0, 2),
            RecipientType(21, "BASCULA", 1.0, 3),
        )
    }
}