package common

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    config = files("$rootDir/config/detekt/detekt-config.yml")
    baseline = file("$rootDir/config/detekt/detekt-baseline.xml")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "11"
    autoCorrect = true
    parallel = true
    buildUponDefaultConfig = false // preconfigure defaults
    ignoreFailures = false

    source = fileTree(projectDir)

    include(kotlinFiles)
    include(kotlinScriptFiles)
    exclude(resourceFiles)
    exclude(buildFiles)

    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("build/reports/detekt.xml"))
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
        txt.required.set(true)
        txt.outputLocation.set(file("build/reports/detekt.txt"))
        sarif.required.set(true)
        sarif.outputLocation.set(file("build/reports/detekt.sarif"))
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "11"
    ignoreFailures.set(true)
    parallel.set(true)
    source = fileTree(projectDir)

    include(kotlinFiles)
    include(kotlinScriptFiles)
    exclude(resourceFiles)
    exclude(buildFiles)
}

val kotlinFiles = "**/*.kt"
val kotlinScriptFiles = "**/*.kts"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
}
