plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate.orm:hibernate-core:6.6.13.Final")
    implementation("com.google.code.gson:gson:2.13.0")

    // database connector
    runtimeOnly("com.mysql:mysql-connector-j:9.2.0")

    // It's also highly recommended to include a logging implementation
    // SLF4J is the standard facade, Logback or Log4j2 are common implementations
//    implementation("org.slf4j:slf4j-api:2.0.13")
//    runtimeOnly("ch.qos.logback:logback-classic:1.5.6")
    // test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
