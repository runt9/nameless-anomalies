package com.runt9.namelessAnomalies.view.duringRun.ui.actionSelect

import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class ActionSelectViewModel : ViewModel() {
    val showingSkills = Binding(false)
    val skillOptions = ListBinding(listOf<Skill>())
    val canInteract = Binding(true)
}
