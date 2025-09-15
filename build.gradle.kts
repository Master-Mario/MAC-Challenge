plugins {
    kotlin("jvm") version "2.0.20"
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

group = "at.mario"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.onarandombox.com/content/groups/public/")
}

dependencies {
    implementation("de.miraculixx:kpaper:1.1.1")
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    implementation("dev.jorel", "commandapi-bukkit-shade", "9.5.2")
    implementation("dev.jorel", "commandapi-bukkit-kotlin", "9.5.2")
    compileOnly("com.onarandombox.multiversecore:multiverse-core:4.3.12")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}