plugins {
    java
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
}

dependencies {
    implementation(project(":yukicommons-api"))

    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
