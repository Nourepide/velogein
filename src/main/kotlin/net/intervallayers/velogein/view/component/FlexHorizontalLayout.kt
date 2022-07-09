package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

open class FlexHorizontalLayout : HorizontalLayout {

    constructor() : super()
    constructor(vararg children: Component?) : super(*children)

    init {
        with(style) {
            remove("width")
        }
    }

}
