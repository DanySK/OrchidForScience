import org.codehaus.groovy.tools.shell.util.Logger.io
import org.gradle.internal.impldep.org.bouncycastle.crypto.tls.ConnectionEnd.client
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    `signing`
    kotlin("jvm") version Versions.org_jetbrains_kotlin
    id("de.fayard.buildSrcVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
    id ("org.danilopianini.git-sensitive-semantic-versioning") version Versions.org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin
    id("org.danilopianini.publish-on-central") version Versions.org_danilopianini_publish_on_central_gradle_plugin
}

gitSemVer {
    version = computeGitSemVer() // THIS IS MANDATORY, AND MUST BE LAST IN BLOCK
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("io.ktor:ktor-client-cio:1.2.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation(Libs.orchidcore)
    implementation(Libs.kotlin_stdlib)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

if (System.getenv("CI") == true.toString()) {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

group = "org.danilopianini"
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
