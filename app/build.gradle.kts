import extensions.implementation
import extensions.testImplementation
import extensions.testRuntime
import java.util.Properties
import java.io.FileInputStream
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("common.dependency-updates-check")
    id("com.google.firebase.crashlytics")
    id("common.jacoco-report")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.kover)
}

android {
    namespace = "com.deimosdev.androidblueprintgithub"
    compileSdk = BaseConfig.CompileSdk

    defaultConfig {
        applicationId = "com.deimosdev.androidblueprintgithub"
        minSdk = BaseConfig.MinSdk
        targetSdk = BaseConfig.TargetSdk
        versionCode = BaseConfig.VersionCode
        versionName = BaseConfig.VersionName
        testInstrumentationRunner = BaseConfig.AndroidJunitRunner
        multiDexEnabled = true

        flavorDimensions += listOf("environment")
        manifestPlaceholders["appLabel"] = BaseConfig.DEFAULT_APP_LABEL
    }

    signingConfigs {
//        register("development") {
//            fetchProperties("development").let {
//                storeFile = file(it.getProperty("ANDROID_KEYSTORE_FILE"))
//                storePassword = it.getProperty("ANDROID_KEYSTORE_PASSWORD")
//                keyAlias = it.getProperty("ANDROID_KEY_ALIAS")
//                keyPassword = it.getProperty("KEY_PASSWORD")
//            }
//        }
//        register("staging") {
//            fetchProperties("staging").let {
//                storeFile = file(it.getProperty("ANDROID_KEYSTORE_FILE"))
//                storePassword = it.getProperty("ANDROID_KEYSTORE_PASSWORD")
//                keyAlias = it.getProperty("ANDROID_KEY_ALIAS")
//                keyPassword = it.getProperty("KEY_PASSWORD")
//            }
//        }
        register("production") {
            fetchProperties("production").let {
                storeFile = file("/app/android-keystore.jks")
                storePassword = it.getProperty("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = it.getProperty("ANDROID_KEY_ALIAS")
                keyPassword = it.getProperty("KEY_PASSWORD")
            }
        }
    }

    tasks.register<Copy>("movePrePushFile") {
        val prePushFile = file("hooks/pre-push")
        val gitHooksDir = file(".git/hooks")

        if (prePushFile.exists()) {
            from(prePushFile)
            into(gitHooksDir)
            rename("pre-push", "pre-push") // rename to the same name
            include("**/*.sh") // only include files with no extension

            doLast {
                // Make the file executable
                val prePushHook = gitHooksDir.toPath().resolve("pre-push").toFile()
                prePushHook.setExecutable(true)
            }
        }
    }

    productFlavors {
        register("development") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            versionCode = BaseConfig.VersionCode + 1
            dimension = flavorDimensions[0]
            buildConfigField("String", "BASE_URL", BaseConfig.DEV_API_URL)
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
            manifestPlaceholders["appLabel"] = BaseConfig.DEV_APP_LABEL
//            signingConfig = signingConfigs.getByName("development")
        }
        register("staging") {
            applicationIdSuffix = ".qa"
            versionNameSuffix = "-qa"
            versionCode = BaseConfig.VersionCode + 2
            dimension = flavorDimensions[0]
            buildConfigField("String", "BASE_URL", BaseConfig.DEV_API_URL)
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
            manifestPlaceholders["appLabel"] = BaseConfig.STAGING_APP_LABEL
        }
        register("production") {
            applicationIdSuffix = ".prod"
            versionNameSuffix = "-prod"
            versionCode = BaseConfig.VersionCode + 3
            dimension = flavorDimensions[0]
            buildConfigField("String", "BASE_URL", BaseConfig.PROD_API_URL)
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
            manifestPlaceholders["appLabel"] = BaseConfig.PRODUCTION_APP_LABEL
//            signingConfigs.getByName("production")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
//            signingConfig = signingConfigs.getByName("productionRelease")
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }

        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage  = true
//            signingConfig = signingConfigs.getByName("debug")
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,gradle-plugins}"
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}
kapt {
    correctErrorTypes = true
}
dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("com.google.firebase:firebase-crashlytics:18.2.9")
    implementation("com.google.firebase:firebase-analytics:20.1.1")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.compose.material:material:1.4.2")
    addModulesDependencies()
    implementation(libs.bundles.di)
    kapt(libs.hilt.compiler)
    testImplementation(libs.bundles.local.test)
    androidTestImplementation(libs.bundles.integration.test)
    testRuntime(libs.junit.jupiter.engine)
    implementation(platform(libs.firebaseDom))
    implementation(libs.bundles.firebase)
}

dependencies {
    kover(project(":data"))
    kover(project(":presentation"))
    kover(project(":domain"))
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
            packages("*.dagger.*", "*.hilt.*")
            annotatedBy("*Generated*", "*Factory", "*hilt*")
        }
    }

    defaults {
        mergeWith("developmentDebug")
        verify {
            onCheck = true
            rule {
                isEnabled = true
                entity = kotlinx.kover.gradle.plugin.dsl.GroupingEntityType.APPLICATION
                bound {
                    minValue = 10
                    metric = kotlinx.kover.gradle.plugin.dsl.MetricType.LINE
                }
            }
        }
    }
}

android.applicationVariants.all {
    val variantName = name
    kotlin.sourceSets {
        getByName("main") {
            kotlin.srcDir(File("build/generated/kapt/$variantName/kotlin"))
        }
    }
//    if (variant.name == "") {
//        variant.signingConfig = signingConfigs.getByName("debug")
//    }
}



fun DependencyHandlerScope.addModulesDependencies() {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
    implementation(project(mapOf("path" to ":shared:frameworkutils")))
    implementation(project(mapOf("path" to ":shared:testutils")))
    implementation(project(mapOf("path" to ":data")))
    implementation("androidx.room:room-ktx:2.4.2")

}

fun fetchProperties(fileName: String): Properties =
    Properties().apply {
        print("path - " + rootDir.absolutePath.plus("/app/"))
        val configPath =
            rootDir.absolutePath.plus("/app/")
        print(configPath)
        load(FileInputStream(File(configPath, fileName.plus(".properties"))))
    }


