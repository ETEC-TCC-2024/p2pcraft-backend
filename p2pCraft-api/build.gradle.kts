plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "io.github.seujorgenochurras"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.5")
    runtimeOnly("com.mysql:mysql-connector-j:8.4.0")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
