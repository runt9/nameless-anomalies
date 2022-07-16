package com.runt9.namelessAnomalies

import com.badlogic.gdx.Application
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.runt9.namelessAnomalies.config.ApplicationInitializer
import com.runt9.namelessAnomalies.config.Injector
import com.runt9.namelessAnomalies.model.event.ChangeScreenRequest
import com.runt9.namelessAnomalies.model.event.ExitRequest
import com.runt9.namelessAnomalies.util.ext.inject
import com.runt9.namelessAnomalies.util.ext.lazyInject
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.core.NamelessAnomaliesScreen
import com.runt9.namelessAnomalies.view.anomalySelect.AnomalySelectController
import com.runt9.namelessAnomalies.view.duringRun.DuringRunScreen
import com.runt9.namelessAnomalies.view.loading.LoadingScreenController
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.onRenderingThread

class NamelessAnomaliesGame : KtxGame<KtxScreen>() {
    private val logger = naLogger()
    private val initializer by lazyInject<ApplicationInitializer>()
    private val input by lazyInject<Input>()
    private val eventBus by lazyInject<EventBus>()
    private val app by lazyInject<Application>()

    override fun create() {
        initializer.initialize()

        Injector.initGdxDeps()
        Injector.initRunningDeps()

        input.inputProcessor = inject<InputMultiplexer>()
        eventBus.registerHandlers(this)

        addScreen<LoadingScreenController>()
        addScreen<MainMenuScreenController>()
        addScreen<AnomalySelectController>()
        addScreen<DuringRunScreen>()
        setScreen<LoadingScreenController>()
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
        initializer.shutdown()
    }

    @HandlesEvent
    suspend fun changeScreen(event: ChangeScreenRequest<*>) = onRenderingThread {
        logger.debug { "Changing screen to ${event.screenClass.simpleName}" }
        setScreen(event.screenClass.java)
    }

    @HandlesEvent
    @Suppress("UnusedPrivateMember")
    fun handleExit(event: ExitRequest) {
        app.exit()
    }

    private inline fun <reified S : NamelessAnomaliesScreen> addScreen() = addScreen(inject<S>())
}
