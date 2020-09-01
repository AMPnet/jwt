import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version "2.2.8.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    `maven-publish`
}

group = "com.github.AMPnet"
version = "0.1.5"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    val jjwtVersion = "0.11.2"

    implementation("org.springframework.security:spring-security-web")
    implementation("javax.servlet:javax.servlet-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    implementation("io.github.microutils:kotlin-logging:1.8.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

val jar: Jar by tasks
val bootJar: BootJar by tasks
bootJar.enabled = false
jar.enabled = true

publishing {
    publications {
        val sourcesJar by tasks.registering(Jar::class) {
            archiveClassifier.set("sources")
            from(sourceSets.main.get().allSource)
        }
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
