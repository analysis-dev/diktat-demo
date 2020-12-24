import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    `maven-publish`
    kotlin("multiplatform") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    id("dev.fritz2.fritz2-gradle") version "0.8"
    id("org.springframework.boot") version "2.4.1"
    id("org.cqfn.diktat.diktat-gradle-plugin") version "0.1.7"
}

repositories {
    jcenter()
}

val kotlinVersion = "1.4.21"
val serializationVersion = "1.0.1"
val diktatVersion = "0.1.7"
val ktlintVersion = "0.39.0"
val springBootVersion = "2.4.1"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ord.cqfn.diktat"
            version = project.version as String
            description = "diktat-demo"
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

kotlin {
    js(LEGACY).browser {
        repositories {
            jcenter()
            maven("https://kotlin.bintray.com/js-externals")
        }
    }

    jvm {
        repositories {
            mavenLocal()
            mavenCentral()
        }
        withJava()
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }

        getByName("jvmMain") {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
                implementation("org.cqfn.diktat:diktat-common:$diktatVersion") {
                    // exclude to use logback provided by spring
                    exclude("org.slf4j", "slf4j-log4j12")
                }
                implementation("org.cqfn.diktat:diktat-rules:$diktatVersion") {
                    exclude("org.slf4j", "slf4j-log4j12")
                }
                implementation("com.pinterest.ktlint:ktlint-core:$ktlintVersion")
                implementation("com.pinterest.ktlint:ktlint-ruleset-standard:$ktlintVersion")
            }
        }

        getByName("jvmTest") {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
            }
        }

        getByName("jsMain") {
            dependencies {
                implementation(kotlin("stdlib-js"))
                compileOnly("kotlin.js.externals:kotlin-js-jquery:3.2.0-0")
                implementation(npm("ace-builds", "1.4.11"))
            }
        }
    }
}

tasks.getByName("jvmMainClasses") {
    dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    doLast {
        mkdir("build/processedResources/jvm/main/static")
        copy {
            from("$buildDir/distributions")
            into("build/processedResources/jvm/main/static")
        }
    }
}

tasks.getByName<BootJar>("bootJar") {
    requiresUnpack("**/kotlin-compiler-embeddable-*.jar")
}

diktat {
    inputs = files("src/*/kotlin/**/*.kt")
}
