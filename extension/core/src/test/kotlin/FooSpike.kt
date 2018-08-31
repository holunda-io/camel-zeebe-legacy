package io.zeebe.camel

import org.apache.camel.impl.DefaultCamelContext
import org.junit.Test

class FooSpike {


  @Test
  fun name() {
    DefaultCamelContext().start()
  }
}
