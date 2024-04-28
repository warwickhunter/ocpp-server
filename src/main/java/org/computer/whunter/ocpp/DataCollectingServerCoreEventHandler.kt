/*
 * Copyright (c) 2024 Warwick Hunter - All Rights Reserved
 */

package org.computer.whunter.ocpp

import eu.chargetime.ocpp.feature.profile.ServerCoreEventHandler
import eu.chargetime.ocpp.model.Request
import eu.chargetime.ocpp.model.RequestWithId
import eu.chargetime.ocpp.model.core.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.*

class DataCollectingServerCoreEventHandler : ServerCoreEventHandler {

    companion object {
        private const val HEARTBEAT_INTERVAL = 60 // seconds
    }

    interface Listener {
        fun onBootNotificationRequest(request: BootNotificationRequest)
    }

    private var listeners = mutableListOf<Listener>()

    private var receivedRequest: Request? = null
    private var currentIdentifier: String? = null
    public var currentSessionIndex: UUID? = null
    private var transactionId = 42
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeAllListeners() {
        listeners.clear()
    }

    private fun notifyListeners(request: BootNotificationRequest) {
        listeners.forEach {
            try {
                it.onBootNotificationRequest(request)
            } catch (e: Throwable) {
                logger.warn(e.toString())
            }
        }
    }

    override fun handleAuthorizeRequest(
        sessionIndex: UUID?, request: AuthorizeRequest
    ): AuthorizeConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex, request.idTag)
        val tagInfo = IdTagInfo(AuthorizationStatus.Accepted)
        val expiryDate = ZonedDateTime.now().plusDays(1)
        tagInfo.expiryDate = expiryDate
        return AuthorizeConfirmation(tagInfo)
    }

    override fun handleBootNotificationRequest(
        sessionIndex: UUID?, request: BootNotificationRequest
    ): BootNotificationConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex)
        notifyListeners(request)
        return BootNotificationConfirmation(ZonedDateTime.now(), HEARTBEAT_INTERVAL,  RegistrationStatus.Accepted)
    }

    override fun handleDataTransferRequest(
        sessionIndex: UUID?, request: DataTransferRequest
    ): DataTransferConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex)
        return DataTransferConfirmation(DataTransferStatus.Accepted)
    }

    override fun handleHeartbeatRequest(
        sessionIndex: UUID?, request: HeartbeatRequest
    ): HeartbeatConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex)
        return HeartbeatConfirmation(ZonedDateTime.now())
    }

    override fun handleMeterValuesRequest(
        sessionIndex: UUID?, request: MeterValuesRequest
    ): MeterValuesConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex)
        val meterValuesRequest = request
        if (meterValuesRequest.meterValue != null) {
            for (meterValue in meterValuesRequest.meterValue) {
                logger.debug(meterValue.toString())
                if (meterValue.sampledValue != null) {
                    for (sampledValue in meterValue.sampledValue) {
                        logger.debug(sampledValue.toString())
                    }
                }
            }
        }
        return MeterValuesConfirmation()
    }

    override fun handleStartTransactionRequest(
        sessionIndex: UUID?, request: StartTransactionRequest
    ): StartTransactionConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex, request.idTag)
        val tagInfo = IdTagInfo(AuthorizationStatus.Accepted)
        val txnId = transactionId++
        return StartTransactionConfirmation(tagInfo, txnId)
    }

    override fun handleStatusNotificationRequest(
        sessionIndex: UUID?, request: StatusNotificationRequest
    ): StatusNotificationConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex)
        return StatusNotificationConfirmation()
    }

    override fun handleStopTransactionRequest(
        sessionIndex: UUID?, request: StopTransactionRequest
    ): StopTransactionConfirmation {
        logger.debug(request.toString())
        record(request, sessionIndex, request.idTag)
        return StopTransactionConfirmation()
    }

    private fun record(request: RequestWithId, sessionIndex: UUID?, idTag: String? = null) {
        receivedRequest = request
        idTag?.let { currentIdentifier = it }
        sessionIndex?.let { currentSessionIndex = it }
    }
}