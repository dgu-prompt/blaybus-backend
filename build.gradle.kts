plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.0"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.dgu.prompt"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
	google() // Google library
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter")

	implementation ("javax.servlet:javax.servlet-api:3.1.0")
	// MySQL JDBC Driver
	implementation ("mysql:mysql-connector-java:8.0.33")
	// Spring Data JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.1")

//	// Google API Client Library
//	implementation("com.google.api-client:google-api-client:1.33.4")
//	implementation("com.google.oauth-client:google-oauth-client:1.33.1")

	implementation("com.google.api-client:google-api-client:1.34.0")
	implementation("com.google.api-client:google-api-client-gson:1.34.0")
	implementation("com.google.auth:google-auth-library-oauth2-http:1.16.0")
	implementation(files("libs/google-api-services-sheets-v4-rev20230227-2.0.0.jar"))
//	implementation("com.google.code.gson:gson:2.8.9")
//	implementation("com.google.code.gson:gson:2.10.1") // 최신 버전 사용
	implementation(files("libs/gson-2.11.0.jar"))


	implementation("com.google.api-client:google-api-client:1.34.0")


//	implementation("com.google.apis:google-api-services-sheets:v4-rev20230816-2.0.0")
//	implementation("com.google.apis:google-api-services-sheets:v4-rev20231024-2.0.0")


//	// Google HTTP Client with Gson
//	implementation("com.google.http-client:google-http-client-gson:1.41.8")
//	// Google Sheets API
//	implementation("com.google.apis:google-api-services-sheets:v4-rev20230227-1.35.0")
//	implementation("com.google.code.gson:gson:2.10")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	// JWT
	implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")

	testImplementation("com.google.code.gson:gson:2.8.9")
	testImplementation("com.google.api-client:google-api-client:1.34.0")
	testImplementation("com.google.oauth-client:google-oauth-client:1.34.1")
	testImplementation("com.google.auth:google-auth-library-oauth2-http:1.16.0")

	// FCM
	implementation ("com.google.firebase:firebase-admin:9.1.1")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
