val javaVersion = JavaVersion.VERSION_17
val prometheusVersion = "0.15.0"
val ktorVersion = "2.2.3"
val jacksonVersion = "2.14.1"
val mockkVersion = "1.13.4"
val kotestVersion = "5.5.5"
val testContainersVersion = "1.17.6"


plugins {
    application
    kotlin("jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.15.0"
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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("com.github.navikt:rapids-and-rivers:2022122311551671792919.2bdd972d7bdb")
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("org.threeten:threeten-extra:1.7.2")

    implementation("org.flywaydb:flyway-core:9.15.1")
    // implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.zaxxer:HikariCP-java7:2.4.13")

    runtimeOnly("net.logstash.logback:logstash-logback-encoder:7.3")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.5")
    runtimeOnly("org.postgresql:postgresql:42.5.4")

    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-jvm:$mockkVersion")
    testImplementation("io.mockk:mockk-dsl-jvm:$mockkVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions:$kotestVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    // need quarkus-junit-4-mock because of https://github.com/testcontainers/testcontainers-java/issues/970
    testImplementation("io.quarkus:quarkus-junit4-mock:2.16.3.Final")
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

spotless {
    kotlin {
        ktlint("0.48.2")
    }
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
