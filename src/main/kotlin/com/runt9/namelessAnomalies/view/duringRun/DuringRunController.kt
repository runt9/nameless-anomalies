package com.runt9.namelessAnomalies.view.duringRun

import com.badlogic.gdx.utils.Disposable
import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.event.RunEndEvent
import com.runt9.namelessAnomalies.model.event.RunStateUpdated
import com.runt9.namelessAnomalies.model.event.SkillSelected
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.SkillTargetType
import com.runt9.namelessAnomalies.service.duringRun.RunInitializer
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.service.duringRun.SkillService
import com.runt9.namelessAnomalies.util.ext.loadTexture
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.controller.BasicScreenController
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.duringRun.enemy.EnemyViewModel
import com.runt9.namelessAnomalies.view.duringRun.ui.runEnd.RunEndDialogController
import ktx.assets.async.AssetStorage
import ktx.async.onRenderingThread

class DuringRunController(
    private val eventBus: EventBus,
    private val runInitializer: RunInitializer,
    private val runStateService: RunStateService,
    private val assets: AssetStorage,
    private val skillService: SkillService
) : BasicScreenController() {
    override val vm = DuringRunViewModel()
    override val view = injectView<DuringRunView>()
    private val children = mutableListOf<Controller>()
    private var selectedSkill: Skill? = null
    private lateinit var player: Anomaly

    @HandlesEvent
    suspend fun runEnd(event: RunEndEvent) = onRenderingThread {
        // TODO: Need a proper clear of everything
        eventBus.enqueueShowDialog<RunEndDialogController>()
    }

    @HandlesEvent
    suspend fun runStateUpdated(event: RunStateUpdated) = onRenderingThread {
        val newState = event.newState
        vm.enemies(newState.enemies.map(::EnemyViewModel))
    }

    @HandlesEvent
    suspend fun skillSelected(event: SkillSelected) = onRenderingThread {
        selectedSkill = event.skill
    }

    override fun load() {
        eventBus.registerHandlers(this)
        runInitializer.initialize()
        runStateService.load().apply {
            player = anomaly
            vm.anomaly(assets.loadTexture(anomaly.definition.texture))
            vm.enemies(enemies.map(::EnemyViewModel))
        }
    }

    override fun dispose() {
        children.forEach(Disposable::dispose)
        runInitializer.dispose()
        eventBus.unregisterHandlers(this)
        super.dispose()
    }

    fun addChild(controller: Controller) = children.add(controller)

    fun enemyClicked(enemy: EnemyViewModel) {
        if (selectedSkill == null) return

        val skill = selectedSkill!!

        if (skill.target == SkillTargetType.SINGLE) {
            skillService.useSkill(skill, player, listOf(enemy.enemy))
        }
    }
}
