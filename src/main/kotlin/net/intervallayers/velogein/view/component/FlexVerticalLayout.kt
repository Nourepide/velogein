package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.orderedlayout.VerticalLayout

/**
 * Обёртка над вертикальным слоем.
 * Обнуляет ширину слоя.
 *
 * @author Nourepide@gmail.com
 */
open class FlexVerticalLayout : VerticalLayout() {
    init {
        with(style) {
            remove("width")
        }
    }
}
