import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.50"
    id("com.adarshr.test-logger") version "1.3.1"
    id("com.github.ben-manes.versions") version "0.20.0"
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.adarshr.test-logger")
    }

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.23.4")

        testCompile("org.junit.jupiter:junit-jupiter-api:5.3.0-M1")
        testCompile("org.junit.jupiter:junit-jupiter-params:5.3.0-M1")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.0-M1")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        withType<Test> {
            failFast = true
            useJUnitPlatform()
        }
    }

    kotlin {
        experimental {
            coroutines = Coroutines.ENABLE
        }
    }
}
