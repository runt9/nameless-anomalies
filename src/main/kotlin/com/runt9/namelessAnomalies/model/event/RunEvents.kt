package com.runt9.namelessAnomalies.model.event

import com.runt9.namelessAnomalies.model.RunState
import com.runt9.namelessAnomalies.util.framework.event.Event

class RunStateUpdated(val newState: RunState) : Event
class RunEndEvent(val win: Boolean) : Event
