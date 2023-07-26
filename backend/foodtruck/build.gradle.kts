plugins {
    java
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.connectruck"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // spring data jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // spring data redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // h2 db
    runtimeOnly("com.h2database:h2")

    // mysql
    runtimeOnly("mysql:mysql-connector-java:8.0.33")

    // flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // springdoc openapi (swagger ui)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // spring test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
