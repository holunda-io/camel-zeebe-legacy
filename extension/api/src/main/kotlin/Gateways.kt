@file:Suppress("PackageDirectoryMismatch")
package io.zeebe.camel.api

import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.api.command.StartProcessCommand

interface DeployGateway {
  fun send(command: DeployCommand)
}

interface StartProcessGateway {
  fun send(command: StartProcessCommand<*>)
}
