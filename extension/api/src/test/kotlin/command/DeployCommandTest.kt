package command

import io.zeebe.camel.api.command.DeployCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DeployCommandTest {

    @Test
    fun `create from classpath resource`() {
        val cmd = DeployCommand.of("/foo/bar.xml")

        assertThat(cmd.xml).isEqualTo("<hello>World</hello>")
        assertThat(cmd.name).isEqualTo("bar.xml")

    }
}
