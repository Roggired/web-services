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
}

application {
    mainClass.set("ru.itmo.yofik.webservices.client.App")
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "ru.itmo.yofik.webservices.client.App")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
