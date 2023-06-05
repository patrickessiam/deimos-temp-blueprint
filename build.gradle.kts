// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
    repositories {
        google()
        mavenCentral()
    }
}
@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("common.dependency-updates-check")
    id("common.detekt")
    id("common.dependency-vulnerability-scanner")
    alias(libs.plugins.kotlin.kover) apply false
}

