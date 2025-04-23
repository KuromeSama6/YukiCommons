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

tasks.register("default") {
    group = "build"
    description = "Default build task equivalent to 'clean install'"
    dependsOn("clean", "build")
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
