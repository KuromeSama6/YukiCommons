plugins {
    `java-library`
}

group = "moe.protasis"
version = "1.2.1"
description = "Parent project for YukiCommons"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18)) // Adjust if needed
    }
}

tasks.named("build") {
    finalizedBy(":yukicommons-api:publishToMavenLocal")
    finalizedBy(":yukicommons-core-bukkit:publishToMavenLocal")
    finalizedBy(":yukicommons-core-bungeecord:publishToMavenLocal")
    finalizedBy(":yukicommons-core-velocity:publishToMavenLocal")
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
