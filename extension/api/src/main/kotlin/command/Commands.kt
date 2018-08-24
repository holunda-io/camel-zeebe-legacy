package io.zeebe.camel.api.command

enum class CommandType {
  COMPLETE, FAIL
}


data class SomeCommand(
    val name: String
)
