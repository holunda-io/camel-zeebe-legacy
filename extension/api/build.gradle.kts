import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
  kotlin("jvm")
  id("io.spring.dependency-management") version Versions.springBootDependencyManagement
}

configure<DependencyManagementExtension> {
  imports {
    mavenBom("org.apache.camel:camel-parent:2.22.0")
  }
}

dependencies {
  compile("org.apache.camel:camel-core")

  testCompile("junit:junit:4.12")
  testCompile("org.apache.camel:camel-test")
  testCompile("org.slf4j:slf4j-simple:1.7.25")
  testCompile("org.springframework.boot:spring-boot-starter-test:2.0.4.RELEASE")
}
