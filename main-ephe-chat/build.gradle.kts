plugins {
	id ("com.google.cloud.tools.jib")
}



jib {
	from{
		image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
	}
	to {
		image = "glavs/ephemeral-chat"
	}
	container {
		creationTime = "USE_CURRENT_TIMESTAMP"
	}
}




dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	compileOnly("org.projectlombok:lombok")
	implementation("com.h2database:h2")
	implementation("org.flywaydb:flyway-core")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.webjars:webjars-locator-core")
	implementation("org.webjars:sockjs-client:1.5.1")
	implementation("org.webjars:stomp-websocket:2.3.4")
	implementation("org.webjars:bootstrap")
	implementation("org.webjars:font-awesome:6.7.1")


	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation("commons-io:commons-io:2.18.0")

}

