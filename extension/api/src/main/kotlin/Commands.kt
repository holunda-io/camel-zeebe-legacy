@file:Suppress("PackageDirectoryMismatch")
package io.zeebe.camel.api.command

import io.zeebe.camel.api.event.JobEvent
import java.io.File

data class DeployCommand(
    val name: String,
    val xml: String
) {
  companion object {
    fun of(resourcePath: String) = with(DeployCommand::class.java.getResource(resourcePath)) {
      DeployCommand(
          File(file).name,
          readText().trim()
      )
    }
  }
}


data class StartProcessCommand<T>(
    val bpmnProcessId: String,
    val payload: T
)

data class CompleteJobCommand<T>(val jobEventJson: JobEvent, val payload: T? = null)
