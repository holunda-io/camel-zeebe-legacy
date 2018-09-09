import org.gradle.api.tasks.bundling.Jar

plugins {
  kotlin("jvm")
}

dependencies {
  testCompile("junit:junit:4.12")
  testCompile("org.assertj:assertj-core:3.10.0")
  testCompile("org.slf4j:slf4j-simple:1.7.25")
}

