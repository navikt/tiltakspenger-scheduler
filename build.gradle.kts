val javaVersion = JavaVersion.VERSION_17
val prometheusVersion = "0.15.0"
val ktorVersion = "2.2.2"
val jacksonVersion = "2.14.1"
val mockkVersion = "1.13.3"
val kotestVersion = "5.5.4"
val testContainersVersion = "1.17.6"


plugins {
    application
    kotlin("jvm") version "1.8.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("ca.cutterslade.analyze") version "1.9.0"
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
    maven("https://jitpack.io")
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
//    implementation("io.prometheus:simpleclient:$prometheusVersion")
//    implementation("io.prometheus:simpleclient_common:$prometheusVersion")
    implementation("com.github.navikt:rapids-and-rivers:2022122311551671792919.2bdd972d7bdb")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("com.natpryce:konfig:1.6.10.0")

    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("org.threeten:threeten-extra:1.7.2")

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jvm:$ktorVersion")
    implementation("io.ktor:ktor-utils-jvm:$ktorVersion")
    implementation("io.ktor:ktor-http-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("org.flywaydb:flyway-core:9.11.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.5.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-dsl-jvm:$mockkVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions:$kotestVersion")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    // need quarkus-junit-4-mock because of https://github.com/testcontainers/testcontainers-java/issues/970
    testImplementation("io.quarkus:quarkus-junit4-mock:2.16.0.Final")
    testImplementation("org.awaitility:awaitility:3.0.0")
}

configurations.all {
    // exclude JUnit 4
    exclude(group = "junit", module = "junit")
}

application {
    mainClass.set("no.nav.tiltakspenger.ApplicationKt")
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("$projectDir/config/detekt.yml")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = javaVersion.toString()
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
    test {
        // JUnit 5 support
        useJUnitPlatform()
        // https://phauer.com/2018/best-practices-unit-testing-kotlin/
        systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    }
    analyzeClassesDependencies {
        warnUsedUndeclared = true
        warnUnusedDeclared = true
    }
    analyzeTestClassesDependencies {
        warnUsedUndeclared = true
        warnUnusedDeclared = true
    }
}
