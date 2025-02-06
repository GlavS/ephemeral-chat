rootProject.name = "ephemeral-chat"
include("main-ephe-chat")
include("panel-admin")


pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val sonarlint: String by settings
    val spotless: String by settings
    val lombok: String by settings


    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
        id("io.freefair.lombok") version lombok
    }
}
