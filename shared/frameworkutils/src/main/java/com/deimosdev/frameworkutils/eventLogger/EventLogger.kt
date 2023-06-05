package com.deimosdev.frameworkutils.eventLogger

interface EventLogger {
    fun logEvent(eventName: String, parameters: Map<String, Any>? = null) {}
}
