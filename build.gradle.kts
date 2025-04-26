plugins {
    `java-library`
}

group = "moe.protasis"
version = "1.2.0"
description = "Parent project for YukiCommons"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18)) // Adjust if needed
    }
}

tasks.named("build") {
    finalizedBy(":yukicommons-api:publishToMavenLocal")
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
