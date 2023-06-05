package common

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.kotlin.kapt")
}

android {
    compileSdk = BaseConfig.CompileSdk

    defaultConfig {
        minSdk = BaseConfig.MinSdk
        targetSdk = BaseConfig.TargetSdk
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = BaseConfig.AndroidJunitRunner
        consumerProguardFiles("consumer-rules.pro")
        flavorDimensions += listOf("environment")
    }

    productFlavors {
        register("development")
        register("staging")
        register("production")
    }

    sourceSets {
        getByName("main").kotlin.srcDir("build/generated/kapt/main/kotlin")
        getByName("test").kotlin.srcDir("build/generated/kapt/test/kotlin")
        getByName("debug").kotlin.srcDir("build/generated/kapt/debug/kotlin")
        getByName("release").kotlin.srcDir("build/generated/kapt/release/kotlin")
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
            enableUnitTestCoverage = true
            enableAndroidTestCoverage   = true
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = true
        ignoreWarnings = true
        quiet = true
        warning.add("InvalidPackage")
        checkOnly.add("HandlerLeak")
        disable.addAll(listOf("StringFormatInvalid", "NewApi"))
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf(
            "-Xjvm-default=all",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlin.Experimental"
        )
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

