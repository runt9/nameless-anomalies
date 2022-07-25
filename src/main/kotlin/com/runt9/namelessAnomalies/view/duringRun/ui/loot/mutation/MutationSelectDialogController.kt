package com.runt9.namelessAnomalies.view.duringRun.ui.loot.mutation

import com.runt9.namelessAnomalies.model.loot.Mutation
import com.runt9.namelessAnomalies.service.duringRun.AttributeService
import com.runt9.namelessAnomalies.service.duringRun.BattleLoot
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView

class MutationSelectDialogController(private val runStateService: RunStateService, private val attributeService: AttributeService, loot: BattleLoot) : DialogController() {
    override val vm = MutationSelectDialogViewModel(loot.mutation1, loot.mutation2)
    override val view = injectView<MutationSelectDialogView>()

    fun mutationChosen(chosen: Mutation, notChosen: Mutation) {
        runStateService.load().anomaly.apply {
            mutations += chosen
            chosen.modifiers.forEach { mod ->
                attrs[mod.type]?.attrMods?.add(mod)
            }
            attributeService.recalculateAttrs(this)
        }

        // TODO: Add not chosen to boss
        hide()
    }
}
