plugins {
    java
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
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":yukicommons-api"))
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
