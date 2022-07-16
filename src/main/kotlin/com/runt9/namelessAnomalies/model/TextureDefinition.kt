package com.runt9.namelessAnomalies.model

enum class TextureDefinition(textureFile: String) {
    HERO_ARROW("heroArrow-tp.png"),
    BOSS_ARROW("bossArrow-tp.png"),
    GOLD_ARROW("goldArrow-tp.png"),
    BLUE_ARROW("blueArrow-tp.png"),
    RED_ARROW("redArrow-tp.png")
    ;

    val assetFile = "texture/$textureFile"
}
