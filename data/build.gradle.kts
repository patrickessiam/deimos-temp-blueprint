import extensions.*

/**
 * add code coverage plugin Jacoco
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("common.android-config")
    id("dagger.hilt.android.plugin")
    id("common.dependency-updates-check")
    alias(libs.plugins.kotlin.kover)
}

koverReport {
    filters {
        excludes {
            classes(
                "*.databinding.*",
                "*.BuildConfig",
                "*Hilt*", "*Dagger*", "*Factory",
                "*_Provide.*",
                "*_*Factory",
                "*_Factory",
                "*_ProvideFactory",
                "*_SingletonC",
            )
            annotatedBy("*Generated*", "*Factory")
        }
    }
}

android {
    namespace = "com.deimosdev.data"

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    FRAMEWORK
    TEST
    implementation(libs.room.ktx)
    implementation(project(mapOf("path" to ":domain")))
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")
    kapt(libs.room.compiler)
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    implementation(libs.bundles.network)
    testImplementation ("org.mockito:mockito-core:3.9.0")

}
