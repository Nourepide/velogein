package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.html.Hr

/**
 * Главная разделяющая полоса, заранее указан размер и цвет.
 *
 * @author Nourepide@gmail.com
 */
class Separator : Hr() {
    init {
        with(style) {
            set("height", "2px")
            set("opacity", "25%")
            set("background", "var(--lumo-header-text-color)")
        }
    }
}
