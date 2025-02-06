plugins {
    id("com.google.cloud.tools.jib")
}


jib {
    from {
        image = "bellsoft/liberica-openjdk-alpine-musl:21.0.1"
    }
    to {
        image = "glavs/admin-panel"
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("com.github.docker-java:docker-java")
    implementation("com.github.docker-java:docker-java-transport-okhttp:3.4.1")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars:bootstrap")
    implementation("org.webjars:font-awesome:6.7.1")
}

