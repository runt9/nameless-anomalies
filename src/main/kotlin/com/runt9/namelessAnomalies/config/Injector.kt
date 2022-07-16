package com.runt9.namelessAnomalies.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.GdxAI
import com.runt9.namelessAnomalies.NamelessAnomaliesGame
import com.runt9.namelessAnomalies.service.asset.AssetLoader
import com.runt9.namelessAnomalies.service.asset.SkinLoader
import com.runt9.namelessAnomalies.service.duringRun.RunInitializer
import com.runt9.namelessAnomalies.service.duringRun.RunServiceRegistry
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.DialogManager
import com.runt9.namelessAnomalies.view.anomalySelect.AnomalySelectController
import com.runt9.namelessAnomalies.view.duringRun.DuringRunScreen
import com.runt9.namelessAnomalies.view.duringRun.game.DuringRunGameController
import com.runt9.namelessAnomalies.view.duringRun.ui.DuringRunUiController
import com.runt9.namelessAnomalies.view.duringRun.ui.menu.MenuDialogController
import com.runt9.namelessAnomalies.view.duringRun.ui.runEnd.RunEndDialogController
import com.runt9.namelessAnomalies.view.loading.LoadingScreenController
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController
import com.runt9.namelessAnomalies.view.settings.SettingsDialogController
import ktx.inject.Context
import ktx.inject.register

object Injector : Context() {
    fun initStartupDeps() = register {
        bindSingleton<NamelessAnomaliesGame>()
        bindSingleton<PlayerSettingsConfig>()
        bindSingleton<ApplicationConfiguration>()
        bindSingleton<EventBus>()
        bindSingleton<AssetConfig>()
        bindSingleton<SkinLoader>()
        bindSingleton<AssetLoader>()
        bindSingleton<ApplicationInitializer>()
    }

    fun initGdxDeps() = register {
        bindSingleton(Gdx.app)
        bindSingleton(Gdx.audio)
        bindSingleton(Gdx.files)
        bindSingleton(Gdx.gl)
        bindSingleton(Gdx.graphics)
        bindSingleton(Gdx.input)
        bindSingleton(Gdx.net)
    }

    fun initRunningDeps() = register {
        bindSingleton(InputMultiplexer())
        bindSingleton(GdxAI.getTimepiece())

        bindSingleton<RunServiceRegistry>()
        bindSingleton<RunStateService>()

        bindSingleton<RunInitializer>()

        bindSingleton<DialogManager>()
        bindSingleton<LoadingScreenController>()
        bindSingleton<AnomalySelectController>()
        bindSingleton<MainMenuScreenController>()
        bindSingleton<SettingsDialogController>()
        bindSingleton<RunEndDialogController>()
        bindSingleton<DuringRunGameController>()
        bindSingleton<DuringRunUiController>()
        bindSingleton<DuringRunScreen>()
        bindSingleton<MenuDialogController>()
//        bindSingleton<LootDialogController>()
    }
}
