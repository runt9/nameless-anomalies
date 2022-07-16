package com.runt9.namelessAnomalies

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.runt9.namelessAnomalies.config.ApplicationConfiguration
import com.runt9.namelessAnomalies.config.Injector
import com.runt9.namelessAnomalies.util.ext.inject

object NamelessAnomaliesLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Injector.initStartupDeps()
        Lwjgl3Application(inject<NamelessAnomaliesGame>(), inject<ApplicationConfiguration>())
    }
}
