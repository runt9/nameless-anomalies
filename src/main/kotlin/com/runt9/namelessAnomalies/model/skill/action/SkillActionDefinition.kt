package com.runt9.namelessAnomalies.model.skill.action

import com.runt9.namelessAnomalies.service.skillAction.SkillAction
import kotlin.reflect.KClass

abstract class SkillActionDefinition<T>(val actionClass: KClass<out SkillAction<T>>)

