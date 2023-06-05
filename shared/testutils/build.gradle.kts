

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
}

android {
    compileSdk = BaseConfig.CompileSdk
    namespace = "com.deimosdev.shared.testutils"
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
            debug {
                isMinifyEnabled = false
                isShrinkResources = false
            }
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
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlin.Experimental",
            "-Xjvm-default=all"
        )
    }
}

dependencies {
    api(libs.junit)
    api(libs.mockk)
    api(libs.mockito.core)
    api(libs.coroutines.test)
    api(libs.android.junit.ktx)
    api(libs.roboelectric)
}
