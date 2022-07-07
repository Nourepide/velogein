package net.intervallayers.velogein.view

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinServletRequest
import javax.annotation.security.RolesAllowed

@Route
@RolesAllowed("USER")
class MainView : VerticalLayout() {
    private val exitButton = Button("Выйти") {
        VaadinServletRequest.getCurrent().logout()
    }

    init {
        add(exitButton)
    }
}
