package net.intervallayers.velogein.utils

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEvent

/**
 * Абстрактное событие, позволяет прокидывать их при определённых условиях.
 * При наследовании позволяет более точно определять тип события.
 *
 * @author Nourepide@gmail.com
 */
abstract class AbstractEvent<T : Component>(source: T) : ComponentEvent<T>(source, false)
