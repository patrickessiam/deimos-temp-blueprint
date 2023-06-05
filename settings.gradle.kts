dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "android-blueprintgithub"
include(":app")
include(":shared:testutils")
include(":shared:frameworkutils")
include(":domain")
include(":data")
include(":presentation")
