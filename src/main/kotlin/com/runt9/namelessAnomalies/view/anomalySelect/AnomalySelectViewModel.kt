package com.runt9.namelessAnomalies.view.anomalySelect

import com.runt9.namelessAnomalies.model.anomaly.definition.AnomalyDefinition
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class AnomalySelectViewModel(options: List<AnomalyDefinition>) : ViewModel() {
    val seed = Binding("")
    val anomalyOptions = ListBinding(options)
    val selectedAnomaly = Binding(options[0])
}
