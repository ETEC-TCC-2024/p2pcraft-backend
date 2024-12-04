rootProject.name = "p2pCraft-backend"
include("p2pCraft-api")
include("p2pcraft-connection-mod")


pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
