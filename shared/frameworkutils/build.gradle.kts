
plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

android {
    compileSdk = BaseConfig.CompileSdk
    namespace = "com.deimosdev.frameworkutils"
    defaultConfig {
        minSdk = BaseConfig.MinSdk
        targetSdk = BaseConfig.TargetSdk

        testInstrumentationRunner = BaseConfig.AndroidJunitRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.app.compat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.room.ktx)
    implementation(libs.datastore)
    implementation(libs.datastore.core)
    implementation(libs.timber)
    implementation(platform("com.google.firebase:firebase-bom:31.2.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("androidx.security:security-crypto:1.0.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    // For App Authentication APIs
    implementation("androidx.security:security-app-authenticator:1.0.0-alpha02")

    // For App Authentication API testing
    androidTestImplementation("androidx.security:security-app-authenticator:1.0.0-alpha01")
    implementation(libs.firebaseAnalytics)
    implementation(libs.junit)
}
