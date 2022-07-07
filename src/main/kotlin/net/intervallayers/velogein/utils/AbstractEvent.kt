package net.intervallayers.velogein.utils

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEvent

abstract class AbstractEvent<T : Component>(source: T) : ComponentEvent<T>(source, false)
