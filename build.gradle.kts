val javaVersion = JavaVersion.VERSION_21
val prometheusVersion = "0.15.0"
val ktorVersion = "2.3.7"
val jacksonVersion = "2.14.1"
val mockkVersion = "1.13.8"
val kotestVersion = "5.8.0"
val testContainersVersion = "1.19.3"


plugins {
    application
    kotlin("jvm") version "1.9.20"
    id("com.diffplug.spotless") version "6.23.3"
    // id("ca.cutterslade.analyze") version "1.9.1"
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
    maven {
        url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("com.github.navikt:rapids-and-rivers:2023101613431697456627.0cdd93eb696f")
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("org.threeten:threeten-extra:1.7.2")

    implementation("org.flywaydb:flyway-core:10.2.0")
    // implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.zaxxer:HikariCP-java7:2.4.13")

    runtimeOnly("net.logstash.logback:logstash-logback-encoder:7.4")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")
    runtimeOnly("org.postgresql:postgresql:42.7.1")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
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
    testImplementation("io.quarkus:quarkus-junit4-mock:3.6.1")
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
    /*
    analyzeClassesDependencies {
        warnUsedUndeclared = true
        warnUnusedDeclared = true
    }
    analyzeTestClassesDependencies {
        warnUsedUndeclared = true
        warnUnusedDeclared = true
    }
     */
}
