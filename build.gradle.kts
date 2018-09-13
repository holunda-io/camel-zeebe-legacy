plugins {
  base
  idea
}

allprojects {
  group = "io.zeebe.camel"
  version = "0.0.2-SNAPSHOT"

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
