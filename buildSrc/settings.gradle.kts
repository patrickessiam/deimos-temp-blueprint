dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}