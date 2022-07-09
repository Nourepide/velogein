package net.intervallayers.velogein.view.domicile

import com.vaadin.flow.component.html.H2
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import net.intervallayers.velogein.view.layout.MainLayout
import javax.annotation.security.RolesAllowed

@Route(value = "domicile", layout = MainLayout::class)
@PageTitle("Резиденты")
@RolesAllowed("USER")
class DomicileView : FlexVerticalLayout() {
    init {
        val header = FlexVerticalLayout().apply {
            addClassNames(Width.FULL, AlignItems.CENTER)
            H2("Резиденты")
                .apply { addClassName(Margin.NONE) }
                .also { add(it) }
        }

        add(header)
    }
}
