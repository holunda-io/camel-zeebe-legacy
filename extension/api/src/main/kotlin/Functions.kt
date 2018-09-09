@file:Suppress("PackageDirectoryMismatch")
package io.zeebe.camel.api

import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.event.JobEvent


interface JobWorker {
  fun apply(event: JobEvent) : CompleteJobCommand
}
