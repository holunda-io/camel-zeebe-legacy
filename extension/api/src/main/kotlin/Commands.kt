@file:Suppress("PackageDirectoryMismatch")
package io.zeebe.camel.api.command

import io.zeebe.camel.api.Json
import io.zeebe.camel.api.Xml
import java.io.File

data class DeployCommand(
    val name: String,
    val xml: Xml
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

data class CompleteJobCommand(val jobEventJson: Json, val payload: Json? = null)
