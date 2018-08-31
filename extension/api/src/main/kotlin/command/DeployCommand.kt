package io.zeebe.camel.api.command

data class DeployCommand (
    val bpmnResource: String,
    val resourceName: String,
    val topic : String?
) {
  // FIXME: secondary constructor, to read from resource
}
