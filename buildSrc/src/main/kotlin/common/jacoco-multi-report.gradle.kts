package common

plugins {
    base
    `jacoco-report-aggregation`
}

val coverModules = "data, presentation"

val androidFileFilter = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/*\$Lambda$*.*", // Jacoco can not handle several "$" in class name.
    "**/*\$inlined$*.*", // Kotlin specific, Jacoco can not handle several "$" in class name.
    "**/*Companion*.*",
    "**/*Extensions*.*",
    "**/*Test*.*",
    "**/Manifest*.*",
    "android/**/*.*",
    "**/*App.*",
    "**/*Application.*",
    "**/*Activity.*",
    "**/*Fragment.*",
)

val fileFilter = listOf(
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
)

afterEvaluate {
    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = false
            excludes = listOf(
                "jdk.internal.*",
                "org.junit.*",
                "org.hamcrest.*",
                "org.mockito.*",
                "org.robolectric.*",
                "org.jacoco.*",
                "org.gradle.*"
            )
        }
    }

    tasks.register<JacocoReport>("codeCoverageReport") {
        val coverProjects = subprojects.filter { it.name in coverModules }
        val coverSourceDirs = coverProjects.map { it.file("src/main/java") }

        val classDirectoriesTree = mutableListOf<ConfigurableFileTree>()

        coverProjects.forEach {
            println("classDirectoriesTree -> project name: ${it.name}")
            classDirectoriesTree.add(fileTree(it.buildDir) {
                include(
                    "intermediates/javac/developmentDebug/classes/com/deimos/data/*",
                    "intermediates/javac/developmentDebug/classes/com/deimos/presentation/*",
                    "tmp/kotlin-classes/developmentDebug/com/deimos/data/*",
                    "tmp/kotlin-classes/developmentDebug/com/deimos/presentation/*"
                )
                exclude(fileFilter + androidFileFilter)
            })
        }

        reports {
            xml.required.set(false)
            html.required.set(true)
        }

        classDirectories.setFrom(classDirectoriesTree)
        sourceDirectories.setFrom(coverSourceDirs)
        executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

        dependsOn(allprojects.map { it.tasks.named<Test>("test") })

    }
}