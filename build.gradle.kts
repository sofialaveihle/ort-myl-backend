plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("org.hibernate.orm:hibernate-core:6.6.13.Final")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    implementation("org.hibernate:hibernate-hikaricp:6.6.13.Final")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    implementation("com.google.code.gson:gson:2.13.0")

    // database connector
    runtimeOnly("com.mysql:mysql-connector-j:9.2.0")

    implementation("software.amazon.awssdk:s3:2.25.20")

    implementation(project(":ort-myl-dtos"))

    // It's also highly recommended to include a logging implementation
    // SLF4J is the standard facade, Logback or Log4j2 are common implementations
//    implementation("org.slf4j:slf4j-api:2.0.13")
//    runtimeOnly("ch.qos.logback:logback-classic:1.5.6")
    // test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // firebase
    implementation("com.google.firebase:firebase-admin:9.2.0")
}

tasks.test {
    useJUnitPlatform()
}
