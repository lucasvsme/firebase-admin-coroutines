import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.50"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.2")

    compile("com.google.firebase:firebase-admin:5.3.0")
    compile("com.google.api:api-common:1.2.0")

    testCompile("org.junit.jupiter:junit-jupiter-api:5.1.0-RC1")
    testCompile("org.junit.jupiter:junit-jupiter-params:5.1.0-RC1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.1.0-RC1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

kotlin {
    experimental {
        coroutines = Coroutines.ENABLE
    }
}
