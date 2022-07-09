package net.intervallayers.velogein.view.deposit

import com.vaadin.flow.component.html.H2
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import net.intervallayers.velogein.view.layout.MainLayout
import javax.annotation.security.RolesAllowed

@Route(value = "deposit", layout = MainLayout::class)
@PageTitle("Депозиты")
@RolesAllowed("USER")
class DepositView : FlexVerticalLayout() {
    init {
        val header = FlexVerticalLayout().apply {
            addClassNames(Width.FULL, AlignItems.CENTER)
            H2("Депозиты")
                .apply { addClassName(Margin.NONE) }
                .also { add(it) }
        }

        add(header)
    }
}
