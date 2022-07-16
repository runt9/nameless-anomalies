package com.runt9.namelessAnomalies.model.event

import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.util.framework.event.Event

class SkillSelected(val skill: Skill) : Event
