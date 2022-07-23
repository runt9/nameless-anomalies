package com.runt9.namelessAnomalies.view.duringRun.ui.map

import com.runt9.namelessAnomalies.model.graph.MapGraph
import com.runt9.namelessAnomalies.model.graph.node.BattleRoom
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class MapDialogViewModel : ViewModel() {
    val map = Binding(MapGraph(BattleRoom()))
}
