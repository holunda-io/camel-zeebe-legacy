@file:Suppress("PackageDirectoryMismatch")
package io.zeebe.camel.api.event

import io.zeebe.camel.api.Json

data class JobCreatedEvent(
    val jobType: String,
    val jobEvent: Json,
    val payload: Json?
)
