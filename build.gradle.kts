plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.3"
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    //annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
tasks.assemble {
    dependsOn(tasks.reobfJar)
}
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks.test {
    useJUnitPlatform()
}