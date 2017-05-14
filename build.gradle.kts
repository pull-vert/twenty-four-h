import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
	extra["spring.version"] = "5.0.0.RC1"
    extra["reactor-bom.version"] = "Bismuth-M1"

    repositories {
        mavenCentral()
        maven { setUrl("https://repo.spring.io/milestone") }
        maven { setUrl("https://repo.spring.io/snapshot") }
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.0.0.BUILD-SNAPSHOT")
    }
}

plugins {
    val kotlinVersion = "1.1.2-2"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.2.RELEASE"
}

apply {
    plugin("org.springframework.boot")
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { setUrl("https://repo.spring.io/milestone") }
    maven { setUrl("https://repo.spring.io/snapshot") }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

noArg {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8")

    compile("org.springframework:spring-webflux")

    compile("org.springframework.data:spring-data-mongodb")
    compile("org.mongodb:mongodb-driver-reactivestreams")

    compile("org.slf4j:slf4j-api")

	compile("io.projectreactor:reactor-kotlin-extensions:1.0.0.M2")
    compile("io.projectreactor.ipc:reactor-netty")

    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
    compile("io.jsonwebtoken:jjwt:0.7.0")

    // https://mvnrepository.com/artifact/de.mkammerer/argon2-jvm
    compile("de.mkammerer:argon2-jvm:2.2")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin")

    testCompile("io.projectreactor.addons:reactor-test")

    testCompile("junit:junit")
}