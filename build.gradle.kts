plugins {
    base
    kotlin("jvm") version "1.3.70" apply false
}

allprojects {
    group = "org.kupcimat.survey"
    version = "1.0"

    repositories {
        jcenter()
    }

    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = listOf("-Xjsr305=strict")
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
