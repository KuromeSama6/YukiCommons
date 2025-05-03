plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1" // Maven Shade equivalent
}

group = "moe.protasis"
version = project.parent?.version as String;

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

repositories {
    mavenLocal()
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":yukicommons-api"))
    compileOnly("com.google.code.gson:gson:2.13.0")
    implementation("joda-time:joda-time:2.12.5")
    implementation("io.github.classgraph:classgraph:4.8.174")
    implementation("org.projectlombok:lombok:1.18.30")
    compileOnly("com.github.retrooper:packetevents-api:2.7.0")
    runtimeOnly("com.github.retrooper:packetevents-api:2.7.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
