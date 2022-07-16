package com.runt9.namelessAnomalies.util.framework.ui.core

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxScreen

interface NamelessAnomaliesScreen : KtxScreen {
    val stages: List<NamelessAnomaliesStage>

    override fun render(delta: Float) = stages.forEach { it.render(delta) }
    override fun hide() = stages.forEach(Stage::clear)

    override fun dispose() {
        stages.forEach(Disposable::dispose)
    }
}

