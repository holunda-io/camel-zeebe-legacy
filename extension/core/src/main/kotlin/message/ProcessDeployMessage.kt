package io.zeebe.camel.message

import io.zeebe.camel.api.Xml
import io.zeebe.camel.api.command.DeployCommand
import org.apache.camel.CamelContext
import org.apache.camel.component.file.GenericFileMessage
import org.apache.camel.impl.DefaultMessage
import java.io.File

class ProcessDeployMessage(
    context: CamelContext
) : DefaultMessage(context) {

  constructor(context: CamelContext, name: String, content: Xml) : this(context) {
    setHeader("fileName", name)
    setBody(content, Xml::class.java)
  }

  constructor(msg : GenericFileMessage<*>) : this(
      msg.camelContext,
      msg.genericFile.fileName,
      File(msg.genericFile.absoluteFilePath).readText()
  )

  constructor(context: CamelContext, cmd: DeployCommand) : this(context, cmd.name, cmd.xml)

  fun toCommand() : DeployCommand = DeployCommand(getHeader("fileName",String::class.java), getBody(Xml::class.java))
}
