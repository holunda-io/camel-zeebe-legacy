package io.zeebe.camel.processor

import io.zeebe.camel.api.command.DeployCommand
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.component.file.GenericFileMessage
import java.io.File

object FromFileToProcessDeployCommand : Processor {
  override fun process(exchange: Exchange) = with(exchange) {
    val msg = getIn(GenericFileMessage::class.java)

    val cmd = DeployCommand(
        name = msg.genericFile.fileName,
            xml = File(msg.genericFile.absoluteFilePath).readText()
    )

    `in`.body = cmd
  }
}
