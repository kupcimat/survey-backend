import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.72" apply false
    kotlin("plugin.spring") version "1.3.72" apply false
    id("org.springframework.boot") version "2.3.0.RC1" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
    id("com.google.cloud.tools.jib") version "2.2.0" apply false
}

allprojects {
    group = "org.kupcimat.survey"
    version = "1.0"

    repositories {
        jcenter()
        maven("https://repo.spring.io/milestone")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }

        withType<Test> {
            useJUnitPlatform()

            testLogging {
                events = setOf(PASSED, SKIPPED, FAILED)
                exceptionFormat = FULL
            }
        }
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
