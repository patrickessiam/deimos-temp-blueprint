import extensions.TEST
import extensions.androidTestImplementation
import extensions.implementation

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("common.android-config")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.kover)
}

android {
    namespace = "com.deimosdev.presentation"

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
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

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation(project(mapOf("path" to ":domain")))
    implementation("androidx.compose.material:material:1.4.2")
    implementation("com.google.firebase:firebase-common-ktx:20.3.2")
    implementation("com.google.firebase:firebase-config-ktx:20.0.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(mapOf("path" to ":shared:frameworkutils")))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.fragment:fragment-ktx:1.3.6")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    kapt ("androidx.hilt:hilt-compiler:1.0.0-alpha03")
    implementation("io.coil-kt:coil-compose:2.3.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation (project(mapOf("path" to ":data")))
    androidTestImplementation ("io.mockk:mockk:1.12.0")
    implementation(libs.bundles.di)
    kapt(libs.hilt.compiler)
    TEST
}

fun DependencyHandlerScope.addModulesDependencies() {
    implementation(project(mapOf("path" to ":shared:frameworkutils")))

}
kapt {
    correctErrorTypes = true
}