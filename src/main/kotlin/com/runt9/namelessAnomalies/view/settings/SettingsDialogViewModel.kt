package com.runt9.namelessAnomalies.view.settings

import com.runt9.namelessAnomalies.model.config.PlayerSettings
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class SettingsDialogViewModel(settings: PlayerSettings) : ViewModel() {
    val fullscreen = Binding(settings.fullscreen)
    val vsync = Binding(settings.vsync)
}
