plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "teambydefault"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//For RestAssured
	testImplementation ("io.rest-assured:rest-assured:6.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
	implementation("org.xerial:sqlite-jdbc:3.53.2.0")
	implementation("org.hibernate.orm:hibernate-community-dialects:8.0.0.Alpha1")
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
	testImplementation("com.h2database:h2:2.4.240")
	testImplementation("net.jqwik:jqwik:1.9.2")
	testImplementation("org.seleniumhq.selenium:selenium-java:4.45.0") // Selenium dependency
	testImplementation("io.cucumber:cucumber-spring:7.34.4") // for Spring dependency injection in Cucumber

	// For Cucumber
	// // https://mvnrepository.com/artifact/io.cucumber/cucumber-java
	testImplementation("io.cucumber:cucumber-java:7.33.0")
    // // https://mvnrepository.com/artifact/io.cucumber/cucumber-junit-platform-engine
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.33.0")
    // // https://mvnrepository.com/artifact/org.junit.platform/junit-platform-suite
    testImplementation("org.junit.platform:junit-platform-suite:6.0.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
	// Forward cucumber.* system properties from the command line to the test JVM
	System.getProperties().forEach { (key, value) ->
		if (key.toString().startsWith("cucumber.")) {
			systemProperty(key.toString(), value.toString())
		}
	}
}
