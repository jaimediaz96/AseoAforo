package com.example.aseoaforos.mock.provider

import com.example.aseoaforos.mock.entity.Collect
import com.example.aseoaforos.mock.entity.Truck
import com.example.aseoaforos.mock.entity.Client
import com.example.aseoaforos.mock.entity.CollectType
import com.example.aseoaforos.mock.entity.Crew
import com.example.aseoaforos.mock.entity.FillPercentage
import com.example.aseoaforos.mock.entity.Incident
import com.example.aseoaforos.mock.entity.IncidentType
import com.example.aseoaforos.mock.entity.MicroRoute
import com.example.aseoaforos.mock.entity.NotCollected
import com.example.aseoaforos.mock.entity.Provision
import com.example.aseoaforos.mock.entity.ProvisionPlace
import com.example.aseoaforos.mock.entity.ProvisionType
import com.example.aseoaforos.mock.entity.RecipientType
import com.example.aseoaforos.mock.entity.Refuel
import com.example.aseoaforos.mock.entity.StateMicroRoute
import com.example.aseoaforos.mock.entity.TempClient
import com.example.aseoaforos.mock.entity.Toll
import com.example.aseoaforos.mock.entity.User
import com.example.aseoaforos.util.DateUtil

object Data {

    private lateinit var user: User

    fun setUser(user: User) {
        this.user = user
    }

    fun getUserId(): Long {
        return user.Userid
    }

    fun getUser(): User {
        return user
    }


    private lateinit var microRoute: MicroRoute

    fun setMicroRoute(microRoute: MicroRoute) {
        this.microRoute = microRoute
    }

    fun getMicroRoute(): MicroRoute {
        return microRoute
    }

    fun getMicroRouteId(): Long {
        return microRoute.microRouteId
    }

    fun updateRoutePlate(plate: String) {
        microRoute.plate = plate
    }

    fun updateStateMicroRoute(stateId: Long) {
        microRoute.stateId = stateId
    }

    fun updateDescriptionAbandon(description: String) {
        microRoute.descriptionAbandon = description
    }

    fun updateMicroRouteCollectTime(time: Long) {
        microRoute.microRouteCollectTime = time
    }

    fun updateMicroRouteTripTime(time: Long) {
        microRoute.microRouteTripHomeTime = time
    }

    fun updateMicroRouteTotalCollect(total: Double) {
        microRoute.totalCollect = total
    }

    fun updateMicroRouteDensity(density: Double) {
        microRoute.density = density
    }

    fun updateMicroRouteSign(sign: String) {
        microRoute.microRouteSign = sign
    }


    private val microRouteCrewList: MutableList<Crew> = mutableListOf()

    fun getMicroRouteCrewList(): MutableList<Crew> {
        return microRouteCrewList
    }


    private lateinit var truck: Truck

    fun setTruck(truck: Truck) {
        this.truck = truck
    }

    fun getTruckCapacity(): Double {
        return truck.capacity
    }


    private val clientList: MutableList<Client> = mutableListOf()

    fun setClientList(clientList: List<Client>) {
        this.clientList.addAll(clientList)
    }

    fun getClientList(): MutableList<Client> {
        return clientList
    }

    fun getClientsSize(): String {
        return clientList.size.toString()
    }


    private val collectList = mutableListOf<Collect>()

    fun setCollectList() {
        clientList.forEach {
            collectList.add(
                Collect(
                    collectTypeId = it.collectTypeId,
                    collectDate = DateUtil.getCurrentDate(),
                    microRouteId = getMicroRouteId(),
                    clientId = it.clientId,
                )
            )
        }
    }

    fun getCollectList(): MutableList<Collect> {
        return collectList
    }

    fun addCollect(collect: Collect) {
        collectList.add(collect)
    }


    private val tempClientList = mutableListOf<TempClient>()

    fun getTempClientList(): MutableList<TempClient> {
        return tempClientList
    }


    private val refuelList = mutableListOf<Refuel>()

    fun getRefuelList(): MutableList<Refuel> {
        return refuelList
    }


    private val incidentList = mutableListOf<Incident>()

    fun getIncidentList(): MutableList<Incident> {
        return incidentList
    }


    private val provisionList = mutableListOf<Provision>()

    fun getProvisionList(): MutableList<Provision> {
        return provisionList
    }


    // STATIC LISTS
    private val stateMicroRouteList: MutableList<StateMicroRoute> = mutableListOf()

    private val crewList: MutableList<Crew> = mutableListOf()

    private val provisionTypeList: MutableList<ProvisionType> = mutableListOf()

    private val provisionPlaceList: MutableList<ProvisionPlace> = mutableListOf()

    private val tollList: MutableList<Toll> = mutableListOf()

    private val incidentTypeList: MutableList<IncidentType> = mutableListOf()

    private val notCollectedList: MutableList<NotCollected> = mutableListOf()

    private val fillPercentageList: MutableList<FillPercentage> = mutableListOf()

    private val collectTypeList: MutableList<CollectType> = mutableListOf()

    private val recipientTypeList: MutableList<RecipientType> = mutableListOf()

    fun setStateMicroRouteList(stateMicroRouteList: List<StateMicroRoute>) {
        this.stateMicroRouteList.addAll(stateMicroRouteList)
    }

    fun setCrewList(crewList: List<Crew>) {
        this.crewList.addAll(crewList)
    }

    fun setProvisionTypeList(provisionTypeList: List<ProvisionType>) {
        this.provisionTypeList.addAll(provisionTypeList)
    }

    fun setProvisionPlaceList(provisionPlaceList: List<ProvisionPlace>) {
        this.provisionPlaceList.addAll(provisionPlaceList)
    }

    fun setTollList(tollList: List<Toll>) {
        this.tollList.addAll(tollList)
    }

    fun setIncidentTypeList(incidentTypeList: List<IncidentType>) {
        this.incidentTypeList.addAll(incidentTypeList)
    }

    fun setNotCollectedList(notCollectedList: List<NotCollected>) {
        this.notCollectedList.addAll(notCollectedList)
    }

    fun setFillPercentageList(fillPercentageList: List<FillPercentage>) {
        this.fillPercentageList.addAll(fillPercentageList)
    }

    fun setCollectTypeList(collectTypeList: List<CollectType>) {
        this.collectTypeList.addAll(collectTypeList)
    }

    fun setRecipientTypeList(recipientTypeList: List<RecipientType>) {
        this.recipientTypeList.addAll(recipientTypeList)
    }

    fun getStateMicroRouteId(state: String): Long? {
        return stateMicroRouteList.find { it.state.equals(state, true) }?.stateMicroRouteId
    }

    fun getCrewList(): MutableList<Crew> {
        return crewList
    }

    fun getProvisionTypeList(): MutableList<ProvisionType> {
        return this.provisionTypeList
    }

    fun getProvisionPlaceList(): MutableList<ProvisionPlace> {
        return provisionPlaceList
    }

    fun getTollList(): MutableList<Toll> {
        return tollList
    }

    fun getIncidentTypeList(): MutableList<IncidentType> {
        return incidentTypeList
    }

    fun getIncidentTypeName(incidentId: Long): String {
        return incidentTypeList.find { it.incidentTypeId == incidentId }!!.name
    }

    fun getNotCollectedList(): MutableList<NotCollected> {
        return notCollectedList
    }

    fun getFillPercentageList(): MutableList<FillPercentage> {
        return fillPercentageList
    }

    fun getFillPercentagePercent(fillPercentageId: Long): Double {
        return fillPercentageList.find { it.fillPercentageId == fillPercentageId }!!.percent
    }

    fun getFillPercentageId(percent: Double): Long {
        return fillPercentageList.find { it.percent == percent }!!.fillPercentageId
    }

    fun getCollectTypeId(type: String): Long? {
        return collectTypeList.find { it.name.equals(type, true) }?.collectTypeId
    }

    fun getCollectTypeName(collectTypeId: Long): String? {
        return collectTypeList.find { it.collectTypeId == collectTypeId }?.name
    }

    fun getRecipientTypeList(): MutableList<RecipientType> {
        return recipientTypeList
    }
}