plugins {
    id("java")
    application
}

group = "ru.itmo.yofik.webservices"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("jakarta.xml.ws:jakarta.xml.ws-api:4.0.2")
    implementation("com.sun.xml.ws:jaxws-ri:4.0.2")

    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("org.hibernate.orm:hibernate-core:7.0.0.Beta1")
    implementation("org.postgresql:postgresql:42.7.4")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.11")
}

application {
    mainClass.set("ru.itmo.yofik.webservices.back.App")
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "ru.itmo.yofik.webservices.back.App")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
