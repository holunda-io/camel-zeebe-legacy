import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

object Versions {
  val kotlin = "1.2.61"
  val springBoot = "2.0.4.RELEASE"
  val springBootDependencyManagement = "1.0.6.RELEASE"
  val camel = "2.22.0"
}

//
//object Dependencies {
//
//  val springBootGradlePlugin  = "org.springframework.boot:spring-boot-gradle-plugin:${Versions.springBoot}"
//  val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
//
//  val kotlinJvm = "org.jetbrains.kotlin.jvm:${Versions.kotlin}"
//  val kotlinSpring = "org.jetbrains.kotlin.plugin.spring:${Versions.kotlin}"
//
//}
