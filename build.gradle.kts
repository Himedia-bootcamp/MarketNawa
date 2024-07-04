plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "marketnawa.be.ott"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {

	// Web
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Devtools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// es
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.13.4")

	// modelMapper
	implementation("org.modelmapper:modelmapper:3.1.1")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
	mainClass.set("marketnawa.be.ott.MarketNawaBeApplicationKt")
}

tasks.jar {
	enabled = true
	manifest {
		attributes["Main-Class"] = "marketnawa.be.ott.MarketNawaBeApplicationKt"
	}
}
