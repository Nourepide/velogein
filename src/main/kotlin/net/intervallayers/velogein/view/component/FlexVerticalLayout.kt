package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout

open class FlexVerticalLayout : VerticalLayout {

    constructor() : super()
    constructor(vararg children: Component?) : super(*children)

    init {
        with(style) {
            remove("width")
        }
    }

}
