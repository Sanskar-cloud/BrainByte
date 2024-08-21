import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree.Companion.test
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetHierarchy.SourceSetTree.Companion.test

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koinVersion: String by project
val kmongoVersion: String by project
val AWS_VERSION: String by project


plugins {
    application
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml:2.3.11")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")

    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")



    // Auth
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")


    // Content Negotiation
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")

//    // KMongo
//    implementation("org.litote.kmongo:kmongo-async:$kmongoVersion")
//    implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongoVersion")

    // Koin core features
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")


    //mongoDB JAVA ReactiveStream Driver
    implementation("org.mongodb:mongodb-driver-reactivestreams:4.8.0")



    // AWS
    implementation(platform("software.amazon.awssdk:bom:$AWS_VERSION"))
    implementation("software.amazon.awssdk:s3")


        implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.0")
    implementation("org.mongodb:bson-kotlinx:5.1.0")
// OR
//    implementation("org.mongodb:bson-kotlin:5.1.0")


    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")


    // Koin
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    // Ktor Test
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    // Kotlin Test
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    // Truth
    testImplementation("com.google.truth:truth:1.1.3")
    implementation(platform("io.insert-koin:koin-bom:$koinVersion"))
    implementation("io.insert-koin:koin-core")
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("org.simplejavamail:simple-java-mail:8.6.3")
    implementation("com.google.code.gson:gson:2.8.8")
        implementation ("commons-codec:commons-codec:1.15")
        implementation ("commons-codec:commons-codec:1.15")

        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-cio:$ktor_version")
        implementation("io.ktor:ktor-client-logging:$ktor_version")
        implementation("com.sendgrid:sendgrid-java:4.7.4")
        implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-client-core:1.6.4")
    implementation("io.ktor:ktor-client-json:1.6.4")
    implementation("io.ktor:ktor-client-serialization:1.6.4")

        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("io.ktor:ktor-server-cors:$ktor_version")
        // other dependencies
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("com.razorpay:razorpay-java:1.4.0")
    implementation("org.json:json:20210307")
    implementation("io.ktor:ktor-client-core:2.3.1")
    implementation("io.ktor:ktor-client-cio:2.3.1")
    implementation("io.ktor:ktor-client-auth:2.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")

    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-webjars-jvm")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")


    implementation("org.webjars:jquery:3.2.1")
    implementation("io.github.smiley4:ktor-swagger-ui:2.9.0")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")



//        implementation("io.ktor:ktor-server-core:$ktor_version")
//        implementation("io.ktor:ktor-server-netty:$ktor_version")
//        implementation("io.ktor:ktor-swagger:$ktor_version")






    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.example.ApplicationKt" // Change to your main class
    }

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
}
tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.test {
    systemProperty("JWT_SECRET", System.getenv("JWT_SECRET"))
}
tasks {
    create("stage").dependsOn("installDist")
}



