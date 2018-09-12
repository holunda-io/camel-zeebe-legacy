@file:Suppress(PACKAGE)
package io.zeebe.camel.api.command

import io.zeebe.camel.api.Json
import io.zeebe.camel.api.PACKAGE
import io.zeebe.camel.api.Xml
import java.io.File

data class DeployProcessCommand(
    val name: String,
    val xml: Xml
) {
  companion object {
    fun of(resourcePath: String) = with(DeployProcessCommand::class.java.getResource(resourcePath)) {
      DeployProcessCommand(
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

data class CompleteJobCommand(
    val jobType: String,
    val workerName: String,
    val jobEventJson: Json,
    val payload: Json? = null
)

data class RegisterJobWorkerCommand(
    val jobType: String,
    val workerName: String,
    val to: String
)
