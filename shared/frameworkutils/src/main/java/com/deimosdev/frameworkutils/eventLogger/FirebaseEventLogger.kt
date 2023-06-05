package com.deimosdev.frameworkutils.eventLogger

import androidx.core.os.bundleOf
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FirebaseEventLogger() : EventLogger {

    override fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        super.logEvent(eventName, parameters)

        val firebaseParameters = parameters?.mapValues { it.value.toString() }
        val bundle = firebaseParameters?.entries?.map { it.key to it.value }?.toTypedArray()
            ?.let { bundleOf(*it) }
        Firebase.analytics.logEvent(eventName, bundle)
    }
}
