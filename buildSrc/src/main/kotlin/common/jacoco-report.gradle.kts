package common

import BaseConfig
import com.android.build.gradle.BaseExtension
import java.util.*

plugins {
    id("jacoco")
}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.8"
}


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

fun JacocoReportsContainer.reports() {
    xml.required.set(false)
    html.required.set(true)
}

afterEvaluate {
    if (isAndroidModule(this)) {
        setupAndroidReporting()
    } else {
        setupKotlinReporting()
    }
}

fun isAndroidModule(project: Project): Boolean {
    val isAndroidLibrary = project.plugins.hasPlugin("com.android.library")
    val isAndroidApp = project.plugins.hasPlugin("com.android.application")
    return isAndroidLibrary || isAndroidApp
}

val Project.android: BaseExtension
    get() = extensions.findByName("android") as? BaseExtension
        ?: error(
            "Project '$name' is not an Android module. Can't " +
                    "access 'android' extension."
        )


//Explain to Jacoco where are you .class file
fun getClassDirectories(sourcePath: String, sourceName: String) = fileTree(project.buildDir) {
    include(
        "intermediates/javac/${sourcePath}/classes/com/deimos/data/*",
        "intermediates/javac/${sourcePath}/classes/com/deimos/presentation/*",
        "tmp/kotlin-classes/${sourcePath}/com/deimos/data/*",
        "tmp/kotlin-classes/${sourcePath}/com/deimos/presentation/*"
    )
    println("sourceName: $sourceName")
    exclude(fileFilter + androidFileFilter)
}


//Explain to Jacoco where is your source code
fun getSourceDirectoriesTree(flavorName: String, buildTypeName: String = BaseConfig.debugBuidType) =
    fileTree(project.buildDir) {
        include(
            "src/main/java",
            "src/$flavorName/java",
            "src/$buildTypeName/java",
        )
    }


//As you want to gather all your tests reports, add the ec and exec you want to be took into account when generating the report
fun getExecutionDataTree(testTaskName: String) = fileTree(project.buildDir) {
    include("jacoco/${testTaskName}.exec")
}

fun JacocoCoverageVerification.setDirectories(
    sourceName: String,
    sourcePath: String,
    flavorName: String,
    testTaskName: String
) {
    sourceDirectories.setFrom(getSourceDirectoriesTree(flavorName))
    additionalSourceDirs.setFrom(getSourceDirectoriesTree(flavorName))
    classDirectories.setFrom(getClassDirectories(sourcePath, sourceName))
    executionData.setFrom(getExecutionDataTree(testTaskName))
}

fun JacocoReport.setDirectories(
    sourceName: String,
    sourcePath: String,
    flavorName: String,
    testTaskName: String
) {
    sourceDirectories.setFrom(getSourceDirectoriesTree(flavorName))
    additionalSourceDirs.setFrom(getSourceDirectoriesTree(flavorName))

    classDirectories.setFrom(getClassDirectories(sourcePath, sourceName))
    executionData.setFrom(getExecutionDataTree(testTaskName))
}

fun setupKotlinReporting() {
    if (tasks.findByName("jacocoKotlinTestReport") == null) {
        tasks.register<JacocoReport>("jacocoKotlinTestReport") {
            dependsOn("test")
            group = "kotlinReport"
            description = "Generates Jacoco coverage reports for Kotlin tests."

            // Set up report output formats
            reports {
                xml.required.set(false)
                html.required.set(true)
            }
        }
    }
}

fun setupAndroidReporting() {
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

    var productFlavors = android.productFlavors.map { flavor -> flavor.name }

    if (productFlavors.isEmpty()) {
        productFlavors = productFlavors + ""
    }

    productFlavors.forEach { productFlavorName ->
        val variant: String
        val sourcePath: String
        if (productFlavorName.isEmpty()) {
            variant = BaseConfig.debugBuidType
            sourcePath = BaseConfig.debugBuidType
        } else {
            variant = "${productFlavorName}${BaseConfig.debugBuidType.capitalize()}"
            sourcePath = "${productFlavorName}/${BaseConfig.debugBuidType}"
        }

        val testTaskName = "test${variant.capitalize()}UnitTest"
        println("Task -> $testTaskName")
        println("SourcePath -> $sourcePath")

        registerCodeCoverageTask(
            testTaskName,
            sourcePath,
            variant,
            productFlavorName

        )
    }

}

fun Project.registerCodeCoverageTask(
    testTaskName: String,
    sourcePath: String,
    sourceName: String,
    flavorName: String,
) {
    tasks.register<JacocoReport>("${testTaskName}Coverage") {
        dependsOn(testTaskName)
        group = "JacocoReport"
        description =
            "Generates Jacoco coverage reports for ${sourceName.capitalize(Locale.ENGLISH)} build."

        setDirectories(sourceName, sourcePath, flavorName, testTaskName)

        reports {
            xml.required.set(false)
            html.required.set(true)
        }

        finalizedBy("${testTaskName}CoverageVerification")

        doLast {
            exec {
                commandLine("open", "${project.buildDir}/reports/tests/$testTaskName/index.html")
            }
        }
    }

    tasks.register<JacocoCoverageVerification>("${testTaskName}CoverageVerification") {
        group = "JacocoReport"
        description =
            "Generates coverage ratio(./gradlew ${testTaskName}CoverageVerification -PminCoverage=0.5)"

        setDirectories(sourceName, sourcePath, flavorName, testTaskName)

        violationRules {
            isFailOnViolation = true
            rule {
                limit {
                    minimum =
                        "0.5".toBigDecimal()  //TODO : change this value to your minimum coverage
                }
            }
            rule {
                element = "CLASS"
                includes = listOf("org.gradle")
                limit {
                    counter = "LINE"
                    value = "TOTALCOUNT"
                    minimum = "0.5".toBigDecimal()
                }
            }
        }
    }
}

