plugins {
    java
    id("io.freefair.lombok") version "8.4" // Optional, for Lombok support
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
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.github.retrooper:packetevents-spigot:2.7.0")
    compileOnly("com.github.retrooper:packetevents-bungeecord:2.7.0")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")

    implementation("joda-time:joda-time:2.12.5")
    implementation("org.jooq:jooq:3.14.15")
    implementation("org.jcommander:jcommander:2.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("io.github.classgraph:classgraph:4.8.174")
    implementation("org.apache.ant:ant:1.10.15")
    implementation("com.zaxxer:HikariCP:4.0.3")

    // Optional: avoid downloading javadoc classifier as a real dependency
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT") {
        artifact {
            classifier = "javadoc"
            type = "jar"
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
