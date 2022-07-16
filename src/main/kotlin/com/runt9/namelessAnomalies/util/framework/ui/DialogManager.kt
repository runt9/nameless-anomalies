package com.runt9.namelessAnomalies.util.framework.ui

import com.badlogic.gdx.utils.Disposable
import ktx.async.onRenderingThread
import com.runt9.namelessAnomalies.config.Injector
import com.runt9.namelessAnomalies.model.event.ShowDialogRequest
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.core.NamelessAnomaliesStage

class DialogManager(private val eventBus: EventBus) : Disposable {
    var currentStage: NamelessAnomaliesStage? = null

    init {
        eventBus.registerHandlers(this)
    }

    @HandlesEvent
    suspend fun showDialog(event: ShowDialogRequest<*>) = onRenderingThread {
        currentStage?.run {
            val dialog = Injector.getProvider(event.dialogClass.java)()
            dialog.show(this)
        }
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
    }
}
