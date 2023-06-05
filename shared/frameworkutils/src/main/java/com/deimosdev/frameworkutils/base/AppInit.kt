package com.deimosdev.frameworkutils.base

interface AppInitializer {
    fun init(application: CoreApplication)
}

class AppInitializerImpl(private vararg val initializers: AppInitializer) : AppInitializer {
    override fun init(application: CoreApplication) {
        initializers.forEach {
            it.init(application)
        }
    }
}
