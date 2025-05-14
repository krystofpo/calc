import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.adarshr.test-logger") version "4.0.0"

	// plugin detecting unused and misused dependencies; usage: ./gradlew buildHealth
	// id("com.autonomousapps.dependency-analysis") version "1.19.0"

	val kotlinPluginVersion = "2.0.0"

	id("org.jetbrains.kotlin.plugin.allopen") version kotlinPluginVersion
	kotlin("jvm") version kotlinPluginVersion
	kotlin("plugin.jpa") version kotlinPluginVersion
	kotlin("plugin.spring") version kotlinPluginVersion
	kotlin("kapt") version kotlinPluginVersion

	groovy
	java
}

group = "com.polansky.amino"

repositories {
	mavenCentral()
}

//allOpen {
//	annotation("jakarta.persistence.Entity")
////	annotation("jakarta.persistence.Component")
//	annotation("jakarta.persistence.MappedSuperclass")
//}

dependencies {


	// Kotlin libs
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("io.github.oshai:kotlin-logging:7.0.0")



	// Spring libs

	//spring core
	// at minimum, brings in core autoconfiguration and spring-boot annotations
	implementation("org.springframework.boot:spring-boot-starter")


//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

//	implementation("jakarta.annotation:jakarta.annotation-api")
//	kapt("jakarta.persistence:jakarta.persistence-api")
//	kapt("jakarta.annotation:jakarta.annotation-api")



	// Database
//	runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
//	runtimeOnly("org.liquibase:liquibase-core")

	//logback encoder
	implementation("net.logstash.logback:logstash-logback-encoder:8.0")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	runtimeOnly("org.spockframework:spock-junit4:2.3-groovy-4.0")
	testImplementation("org.spockframework:spock-spring:2.4-M4-groovy-4.0")
	testImplementation("org.testcontainers:spock:1.20.1")
	testImplementation("io.github.joke:spock-mockable:2.3.2")
	testImplementation("io.netty:netty-handler:4.1.117.Final")
	testImplementation("org.bouncycastle:bcpkix-jdk18on:1.80")
}

springBoot {
	buildInfo()
}

kotlin {
	jvmToolchain(17)
}

val integrationTestClassesPattern = "**/*IntegrationSpec*"
val documentationDir = file("documentation")
val documentationSnippetsDir = file("build/generated-snippets")
val addressNormalizationGraphFile = "documentation/ANSProcesses"

tasks {

	withType<Test> {
		useJUnitPlatform()

		testlogger {
			showPassed = false
			showFullStackTraces = true
		}
	}

	register<Test>("unit_tests_only") {
		exclude(integrationTestClassesPattern)
		group = "verification"
	}

	compileTestGroovy {
		dependsOn(compileTestKotlin)
		classpath += files(compileTestKotlin.get().destinationDirectory)
	}

}