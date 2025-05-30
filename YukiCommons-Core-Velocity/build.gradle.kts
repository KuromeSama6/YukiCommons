plugins {
    java
    id("io.freefair.lombok") version "8.4" // Optional, for Lombok
    id("com.github.johnrengelman.shadow") version "8.1.1" // Maven Shade equivalent
    `maven-publish`
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
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net")
    mavenCentral()
}

dependencies {
    compileOnly("com.github.retrooper:packetevents-velocity:2.7.0")
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    implementation("joda-time:joda-time:2.12.5")
    implementation("org.jooq:jooq:3.14.15")
    implementation("org.jcommander:jcommander:2.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("io.github.classgraph:classgraph:4.8.174")
    implementation("org.apache.ant:ant:1.10.15")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("com.mojang:brigadier:1.0.18")

    implementation(project(":yukicommons-api"))
    implementation(project(":yukicommons-common"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("") // Replace default 'all' suffix
        version = project.version.toString()
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "moe.protasis"
            artifactId = "yukicommons-core-velocity"
            version = version.toString()
        }
    }
}