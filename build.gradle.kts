@file:Suppress("UnstableApiUsage")

import com.vaadin.gradle.VaadinBuildFrontendTask
import com.vaadin.gradle.VaadinPrepareFrontendTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.vaadin") version "23.1.3"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    kotlin("plugin.jpa") version "1.7.0"
}

group = "net.intervallayers"
version = "PROD"
java.sourceCompatibility = JavaVersion.VERSION_18

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["vaadinVersion"] = "23.1.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    runtimeOnly("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "18"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

vaadin {
    productionMode = true
    generateBundle = true
    optimizeBundle = true

    with(tasks) {
        withType<VaadinPrepareFrontendTask>()
        withType<VaadinBuildFrontendTask>()
    }
}
