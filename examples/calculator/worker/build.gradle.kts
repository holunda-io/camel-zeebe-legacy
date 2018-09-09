import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.bundling.Jar

plugins {
  java
  id("org.springframework.boot") version Versions.springBoot
}

apply {
  plugin ("io.spring.dependency-management")
}

configure<DependencyManagementExtension> {
  imports {
    mavenBom("org.apache.camel:camel-parent:2.22.0")
    mavenBom("io.zeebe:zb-bom:0.11.0")
  }
}

dependencies {
  compile("org.apache.camel:camel-spring-boot-starter")
  compile(project(":extension:camel-zeebe-api"))
  compile("commons-io:commons-io:2.5")

}
