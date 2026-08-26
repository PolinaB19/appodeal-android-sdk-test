pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Appodeal SDK 4.x core and its individually selected mediation adapters.
        maven(url = "https://artifactory.appodeal.com/appodeal")
    }
}
rootProject.name = "AppodealSdkTest"
include(":app")
