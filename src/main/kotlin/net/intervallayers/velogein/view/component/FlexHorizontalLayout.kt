package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.orderedlayout.HorizontalLayout

/**
 * Обёртка над горизонтальным слоем.
 * Обнуляет ширину слоя.
 *
 * @author Nourepide@gmail.com
 */
open class FlexHorizontalLayout : HorizontalLayout() {
    init {
        with(style) {
            remove("width")
        }
    }
}
