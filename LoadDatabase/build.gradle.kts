plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("software.amazon.awssdk:s3:2.25.20")
    implementation("com.google.code.gson:gson:2.13.0")
    implementation(project(":myl-backend")) // root project, put the name if it's a module
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("com.google.guava:guava:32.1.2-jre") // rate limiter


    implementation ("org.sejda.imageio:webp-imageio:0.1.6")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}