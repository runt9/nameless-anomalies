package com.runt9.namelessAnomalies.model.event

import com.runt9.namelessAnomalies.util.framework.event.Event
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.core.BasicScreen
import kotlin.reflect.KClass

class ChangeScreenRequest<S : BasicScreen>(val screenClass: KClass<S>) : Event
inline fun <reified S : BasicScreen> changeScreenRequest() = ChangeScreenRequest(S::class)
inline fun <reified S : BasicScreen> EventBus.enqueueChangeScreen() = enqueueEventSync(changeScreenRequest<S>())

class ShowDialogRequest<D : DialogController>(val dialogClass: KClass<D>) : Event
inline fun <reified D : DialogController> showDialogRequest() = ShowDialogRequest(D::class)
inline fun <reified S : DialogController> EventBus.enqueueShowDialog() = enqueueEventSync(showDialogRequest<S>())

class ExitRequest : Event
fun EventBus.enqueueExitRequest() = enqueueEventSync(ExitRequest())
