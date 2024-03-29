import org.danilopianini.gradle.mavencentral.mavenCentral
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm")
    id("kotlin-qa")
    id("org.jetbrains.dokka")
    id("org.danilopianini.git-sensitive-semantic-versioning")
    id("org.danilopianini.publish-on-central")
}

repositories {
    mavenCentral()
    jcenter()
}

val additionalTools: Configuration by configurations.creating

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.javaeden.orchid:OrchidCore:_")
    implementation("org.apache.commons:commons-lang3:_")
    implementation("org.danilopianini:khttp:_")
    testImplementation("io.kotest:kotest-runner-junit5:_")
    testImplementation("io.kotest:kotest-assertions-core-jvm:_")
    additionalTools("org.jacoco:org.jacoco.core:_")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

jacoco {
    toolVersion = additionalTools.resolvedConfiguration.resolvedArtifacts
        .find { "jacoco" in it.moduleVersion.id.name }
        ?.moduleVersion?.id?.version
        ?: toolVersion
}

tasks.jacocoTestReport {
    reports {
        // Used by Codecov.io
        xml.isEnabled = true
    }
}

if (System.getenv("CI") == true.toString()) {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

group = "org.danilopianini"
publishOnCentral {
    projectDescription = "An Orchid module with utilities for scientific websites"
    projectLongName = "OrchidForScience"
    repository("https://maven.pkg.github.com/DanySK/OrchidForScience".toLowerCase()) {
        user = "DanySK"
        password = System.getenv("GITHUB_TOKEN")
    }
    repository("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/", "CentralS01") {
        user = mavenCentral().user()
        password = mavenCentral().password()
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name.set("Danilo Pianini")
                        email.set("danilo.pianini@gmail.com")
                        url.set("http://www.danilopianini.org/")
                    }
                }
            }
        }
    }
}
