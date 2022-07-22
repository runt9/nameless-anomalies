package com.runt9.namelessAnomalies.model.event

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.util.framework.event.Event

class SkillSelected(val skill: Skill) : Event
class PlayerTurnReady : Event
class TurnComplete : Event
class HpChanged(val self: Anomaly, hpChange: Float) : Event
class AttributesUpdated(val anomaly: Anomaly) : Event
