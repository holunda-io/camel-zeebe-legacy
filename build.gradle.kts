plugins {
  base
  idea
}

allprojects {
  group = "io.zeebe.camel"
  version = "0.0.1-SNAPSHOT"

  repositories {
    mavenLocal()
    jcenter()
  }
}

dependencies {
  subprojects.forEach {
    archives(it)
  }
}
