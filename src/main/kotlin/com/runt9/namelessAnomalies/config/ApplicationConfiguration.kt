package com.runt9.namelessAnomalies.config

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.runt9.namelessAnomalies.model.config.PlayerSettings
import com.runt9.namelessAnomalies.util.ext.getMatching


class ApplicationConfiguration(settingsConfig: PlayerSettingsConfig) : Lwjgl3ApplicationConfiguration() {
    init {
        val settings = settingsConfig.get()
        setTitle("Nameless Anomalies")
        handleResolution(settings.fullscreen, settings.resolution)
        useVsync(settings.vsync)
        setResizable(false)
    }

    private fun handleResolution(fullscreen: Boolean, resolution: PlayerSettings.Resolution) {
        if (fullscreen) {
            setFullscreenMode(getDisplayModes().getMatching(resolution, getDisplayMode()))
        } else {
            resolution.apply { setWindowedMode(width, height) }
        }
    }
}
