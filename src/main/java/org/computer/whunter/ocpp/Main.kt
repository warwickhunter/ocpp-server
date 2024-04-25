/*
 * Copyright (c) 2024 Warwick Hunter - All Rights Reserved
 */

package org.computer.whunter.ocpp

import eu.chargetime.ocpp.test.FakeCentral
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

fun main() {
    runBlocking {
        val logger = LoggerFactory.getLogger(javaClass)
        logger.info("Running OCPP server")
        runServer()
    }
}

suspend fun runServer() {
    val server = FakeCentral.getSystem(FakeCentral.serverType.JSON)
    try {
        server.started()
        while (server.isStarted) {
            delay(2_000)
        }
    } catch (e: Exception) {
        throw RuntimeException(e)
    } finally {
        server.stopped()
    }
}
