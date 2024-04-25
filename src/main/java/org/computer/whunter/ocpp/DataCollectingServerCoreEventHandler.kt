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

    private var receivedRequest: Request? = null
    private var currentIdentifier: String? = null
    private var currentSessionIndex: UUID? = null
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleAuthorizeRequest(
        sessionIndex: UUID?, request: AuthorizeRequest
    ): AuthorizeConfirmation {
        record(request, sessionIndex, request.idTag)
        val tagInfo = IdTagInfo(AuthorizationStatus.Accepted)
        val expiryDate = ZonedDateTime.now().plusDays(1)
        tagInfo.expiryDate = expiryDate
        return AuthorizeConfirmation(tagInfo)
    }

    override fun handleBootNotificationRequest(
        sessionIndex: UUID?, request: BootNotificationRequest
    ): BootNotificationConfirmation {
        record(request, sessionIndex)
        return BootNotificationConfirmation(ZonedDateTime.now(), 1,  RegistrationStatus.Accepted)
    }

    override fun handleDataTransferRequest(
        sessionIndex: UUID?, request: DataTransferRequest
    ): DataTransferConfirmation {
        record(request, sessionIndex)
        return DataTransferConfirmation(DataTransferStatus.Accepted)
    }

    override fun handleHeartbeatRequest(
        sessionIndex: UUID?, request: HeartbeatRequest
    ): HeartbeatConfirmation {
        record(request, sessionIndex)
        return HeartbeatConfirmation(ZonedDateTime.now())
    }

    override fun handleMeterValuesRequest(
        sessionIndex: UUID?, request: MeterValuesRequest
    ): MeterValuesConfirmation {
        record(request, sessionIndex)
        logger.debug(request.toString())
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
        record(request, sessionIndex, request.idTag)
        val tagInfo = IdTagInfo(AuthorizationStatus.Accepted)
        val transactionId = 42 // TODO: use a real value
        return StartTransactionConfirmation(tagInfo, transactionId)
    }

    override fun handleStatusNotificationRequest(
        sessionIndex: UUID?, request: StatusNotificationRequest
    ): StatusNotificationConfirmation {
        record(request, sessionIndex)
        return StatusNotificationConfirmation()
    }

    override fun handleStopTransactionRequest(
        sessionIndex: UUID?, request: StopTransactionRequest
    ): StopTransactionConfirmation {
        record(request, sessionIndex, request.idTag)
        return StopTransactionConfirmation()
    }

    private fun record(request: RequestWithId, sessionIndex: UUID?, idTag: String? = null) {
        receivedRequest = request
        idTag?.let { currentIdentifier = it }
        sessionIndex?.let { currentSessionIndex = it }
    }
}