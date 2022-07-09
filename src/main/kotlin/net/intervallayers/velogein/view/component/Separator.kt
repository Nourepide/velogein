package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.html.Hr

class Separator : Hr() {
    init {
        with(style) {
            set("height", "2px")
            set("opacity", "25%")
            set("background", "var(--lumo-header-text-color)")
        }
    }
}
