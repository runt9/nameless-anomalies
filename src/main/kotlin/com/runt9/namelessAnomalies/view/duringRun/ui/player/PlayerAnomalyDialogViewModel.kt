package com.runt9.namelessAnomalies.view.duringRun.ui.player

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.definition.prototypeAnomaly
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class PlayerAnomalyDialogViewModel : ViewModel() {
    val anomaly = Binding(Anomaly(prototypeAnomaly, true))
}
