import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    kotlin("jvm")
    id("io.spring.dependency-management")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.data:spring-data-mongodb")
}
