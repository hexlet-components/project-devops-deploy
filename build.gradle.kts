plugins {
    application
    alias(libs.plugins.versions)
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

// java {
// 	toolchain {
// 		languageVersion = JavaLanguageVersion.of(25)
// 	}
// }

application {
    mainClass.set("io.hexlet.project_devops_deploy.DemoApplication")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.springBootStarterWeb)
    // implementation "org.springframework.boot:spring-boot-starter"
    // testImplementation "org.springframework.boot:spring-boot-starter-test"
    // testRuntimeOnly "org.junit.platform:junit-platform-launcher"
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springSecurityTest)
    testImplementation(platform(libs.junitBom))
    testImplementation(libs.junitJupiter)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        eclipse().sortMembersEnabled(true)
        formatAnnotations()
        leadingTabsToSpaces(4)
    }
}
