package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.baseDamage
import com.runt9.namelessAnomalies.model.anomaly.cdr
import com.runt9.namelessAnomalies.model.anomaly.critChance
import com.runt9.namelessAnomalies.model.anomaly.critMulti
import com.runt9.namelessAnomalies.model.anomaly.damageResist
import com.runt9.namelessAnomalies.model.anomaly.dodge
import com.runt9.namelessAnomalies.model.anomaly.maxHp
import com.runt9.namelessAnomalies.model.anomaly.tdr
import com.runt9.namelessAnomalies.model.attribute.Attribute
import com.runt9.namelessAnomalies.model.event.HpChanged
import com.runt9.namelessAnomalies.model.interceptor.InterceptableAdapter
import com.runt9.namelessAnomalies.model.interceptor.InterceptableContext
import com.runt9.namelessAnomalies.model.interceptor.Interceptor
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.AFTER_SKILL_USED
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.BEFORE_COOLDOWN_CALC
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.BEFORE_CRIT_CHECK
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.BEFORE_DAMAGE_CHECK
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.BEFORE_HIT_CHECK
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.BEFORE_SKILL_USED
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.BEFORE_TURN_DELAY_CALC
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.ON_CRIT
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.ON_DODGE
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.ON_HIT
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook.ON_NON_CRIT
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import kotlin.math.roundToInt

class SkillService(
    private val eventBus: EventBus,
    registry: RunServiceRegistry,
    private val runStateService: RunStateService,
    private val randomizerService: RandomizerService
) : RunService(eventBus, registry) {
    private val logger = naLogger()

    fun useSelfSkill(skill: Skill, self: Anomaly) {

    }

    fun useSkill(skill: Skill, user: Anomaly, targets: List<Anomaly>) {
        val context = SkillUseContext(skill, user, targets)
        context.interceptors.putAll(gatherInterceptorsForSkill(context))

        context.intercept(BEFORE_SKILL_USED)

        context.targets.forEach { target ->
            val tag = "[${user.definition.name} (${skill.definition.name}) -> ${target.definition.name}]: "
            val combatStr = StringBuilder(tag)

            context.currentTarget = target

            val defenderDodge = target.dodge.clone()
            context.hitCheck = HitContext(defenderDodge)

            context.intercept(BEFORE_HIT_CHECK)
            
            context.hitCheck.apply {
                dodgeChance.recalculate()

                if (dodgeChance() > 0f) {
                    // Possible optimization is to roll once and compare against all targets' dodge
                    val isDodge = randomizerService.percentChance(dodgeChance(), lucky)

                    if (isDodge) {
                        context.intercept(ON_DODGE)
                        combatStr.append("Attack dodged!")
                        logger.info { combatStr.toString() }
                        return@forEach
                    }
                }
            }
            
            context.intercept(ON_HIT)
            combatStr.append("Attack hits! ")

            // TODO: Skip the next section if non-damaging skill

            val userCritChance = user.critChance.clone()
            context.critCheck = CritContext(userCritChance)

            context.intercept(BEFORE_CRIT_CHECK)

            var isCrit = false
            context.critCheck.apply {
                critChance.recalculate()

                if (critChance() > 0f) {
                    isCrit = randomizerService.percentChance(critChance(), lucky)

                    if (isCrit) {
                        combatStr.append("CRITICAL HIT! Crit multi [${user.critMulti}] ")
                    }

                    context.intercept(if (isCrit) ON_CRIT else ON_NON_CRIT)
                }
            }

            // TODO: Use skill % of base
            val userBaseDamage = user.baseDamage.clone()
            val userCritMultiplier = user.critMulti.clone()
            val targetDamageResist = target.damageResist.clone()
            context.damageCheck = DamageContext(userBaseDamage, isCrit, userCritMultiplier, targetDamageResist)

            context.intercept(BEFORE_DAMAGE_CHECK)

            context.damageCheck.apply {
                recalculate()
                val finalDamageModifier = randomizerService.randomize { it.nextInt(90, 111) } / 100f
                val damageOutput = baseDamage() * (if (isCrit) critMulti() else 1f) * finalDamageModifier / damageResist()

                target.currentHp -= damageOutput.roundToInt()

                combatStr.append("Damage: [Base: $baseDamage | Resist: $damageResist | Final: $damageOutput | Defender HP: ${target.currentHp} / ${target.maxHp}]")

                logger.info { combatStr.toString() }

                // TODO: Make sure consumed properly
                eventBus.enqueueEventSync(HpChanged(target, -damageOutput))
            }
        }

        context.turnDelayCheck = TurnDelayContext(skill.definition.turnDelay, user.tdr)
        context.intercept(BEFORE_TURN_DELAY_CALC)
        context.turnDelayCheck.apply {
            tdr.recalculate()
            val finalDelay = baseTurnDelay / tdr()
            logger.info { "Setting turn delay to $finalDelay" }
            user.turnDelay = finalDelay.roundToInt()
        }

        context.cooldownCheck = CooldownContext(skill.definition.cooldown, user.cdr)
        context.intercept(BEFORE_COOLDOWN_CALC)
        context.cooldownCheck.apply {
            cdr.recalculate()
            // TODO: Decide if a ceil is better here, would mean cooldown could never be 0 though?
            val finalCooldown = baseCooldown / cdr()
            logger.info { "Setting final cooldown to $finalCooldown" }
            // NB: The +1 here is because cooldowns get ticked down at the start of each turn so this ensures the cooldown "starts ticking"
            //   on the next turn after use
            skill.remainingCooldown = finalCooldown.roundToInt() + 1
        }

        context.intercept(AFTER_SKILL_USED)
    }

    private fun gatherInterceptorsForSkill(context: SkillUseContext): MutableMap<InterceptorHook, MutableList<Interceptor<InterceptableContext>>> {
        val interceptors = mutableMapOf<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>()

        val gather = { i: Map<InterceptorHook, MutableList<Interceptor<InterceptableContext>>> ->
            i.forEach { (hook, ints) ->
                val filteredInts = ints.filter { it.canIntercept(context) }
                if (filteredInts.isNotEmpty()) {
                    interceptors[hook] = filteredInts.toMutableList()
                }
            }
        }

        runStateService.load().apply {
            gather(this.interceptors)
        }

        gather(context.skill.definition.interceptors)

        return interceptors
    }

    fun rest(anomaly: Anomaly) {
        logger.info { "Processing rest for ${anomaly.definition.name}" }
        // TODO: Rest interceptors
        anomaly.currentSkills.forEach(Skill::tickDownCooldown)
        // TODO: Decide how we wanna handle turn delay for resting
        anomaly.turnDelay = 50
        val maxHp = anomaly.maxHp().roundToInt()
        if (anomaly.currentHp != maxHp) {
            val healAmount = ((maxHp - anomaly.currentHp) * 0.1f).roundToInt()
            anomaly.currentHp = (anomaly.currentHp + healAmount).coerceAtMost(maxHp)
            eventBus.enqueueEventSync(HpChanged(anomaly, healAmount.toFloat()))
        }
    }
}

class SkillUseContext(val skill: Skill, val user: Anomaly, val targets: List<Anomaly>) : InterceptableAdapter() {
    override val interceptors = mutableMapOf<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>()
    var currentTarget: Anomaly? = null
    lateinit var hitCheck: HitContext
    lateinit var critCheck: CritContext
    lateinit var damageCheck: DamageContext
    lateinit var turnDelayCheck: TurnDelayContext
    lateinit var cooldownCheck: CooldownContext
}

class HitContext(val dodgeChance: Attribute, var lucky: Boolean = false)
class CritContext(val critChance: Attribute, var lucky: Boolean = false)
class DamageContext(val baseDamage: Attribute, val isCrit: Boolean, val critMulti: Attribute, val damageResist: Attribute) {
    fun recalculate() {
        baseDamage.recalculate()
        critMulti.recalculate()
        damageResist.recalculate()
    }
}
class TurnDelayContext(val baseTurnDelay: Int, val tdr: Attribute)
class CooldownContext(val baseCooldown: Int, val cdr: Attribute)
