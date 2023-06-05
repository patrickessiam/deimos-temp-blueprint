package com.deimosdev.androidblueprintgithub

import com.deimosdev.frameworkutils.base.CoreApplication
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : CoreApplication() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
