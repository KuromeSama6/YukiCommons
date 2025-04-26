plugins {
    java
    id("io.freefair.lombok") version "8.4" // Optional, for Lombok
    id("com.github.johnrengelman.shadow") version "8.1.1" // Maven Shade equivalent
}

group = "moe.protasis"
version = "1.2.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

repositories {
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.github.retrooper:packetevents-spigot:2.7.0")

    implementation("joda-time:joda-time:2.12.5")
    implementation("org.jooq:jooq:3.14.15")
    implementation("org.jcommander:jcommander:2.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("io.github.classgraph:classgraph:4.8.174")
    implementation("org.apache.ant:ant:1.10.15")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("fr.mrmicky:fastboard:2.1.4")

    implementation(project(":yukicommons-api"))
    implementation(project(":yukicommons-common"))
    implementation(project(":yukicommons-nms-v1_8_R3"))
    implementation(project(":yukicommons-nms-v1_12_R1"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("") // Replace default 'all' suffix
    }

    build {
        dependsOn(shadowJar)
    }
}