import net.ltgt.gradle.errorprone.errorprone
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.2.0")
    }
}

repositories {
    maven {
        url = uri("https://maven.alsutton.com/")
        content {
            includeGroup("com.alsutton")
        }
    }
    mavenCentral()
}

plugins {
    java
    id("net.ltgt.errorprone")
    jacoco
}

val junitVersion="5.7.0"
val mockitoVersion="3.7.7"
val immutablesVersion = "2.8.2"
val hibernateVersion = "5.4.28.Final"
dependencies {
    implementation("org.hibernate:hibernate-core:${hibernateVersion}")
    implementation("org.hibernate:hibernate-ehcache:${hibernateVersion}")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation(project(":lib:jdbcconfiguration"))
    implementation(project(":lib:model"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockitoVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    errorprone("com.google.errorprone:error_prone_core:2.5.1")
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone.allErrorsAsWarnings.set(false)
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}

jacoco {
    toolVersion = "0.8.2"
}

tasks.withType<JacocoReport> {
    group = "Reporting"
    reports {
        xml.isEnabled = true
        csv.isEnabled =  false
        html.destination = file("${buildDir}/reports/coverage")
    }
}