@file:Suppress(PACKAGE)
package io.zeebe.camel.api.event

import io.zeebe.camel.api.Json
import io.zeebe.camel.api.PACKAGE

data class JobCreatedEvent(
    val jobType: String,
    val workerName: String,
    val jobEvent: Json,
    val payload: Json?
)
