plugins {
    id("java")
    id("com.diffplug.spotless") version "7.0.0.BETA4"
}
repositories {
    mavenCentral()
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
tasks.test {
    useJUnitPlatform()
}
subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        java {
            eclipse()
                .configFile(rootDir.path + "/.formatter/Default.xml")
        }
    }
}
