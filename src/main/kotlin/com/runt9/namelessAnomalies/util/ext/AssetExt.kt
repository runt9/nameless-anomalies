package com.runt9.namelessAnomalies.util.ext

import com.badlogic.gdx.graphics.Texture
import com.runt9.namelessAnomalies.model.TextureDefinition
import ktx.assets.async.AssetStorage

fun AssetStorage.loadTexture(def: TextureDefinition): Texture = this[def.assetFile]
