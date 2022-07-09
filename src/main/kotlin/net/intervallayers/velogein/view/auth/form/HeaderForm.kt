package net.intervallayers.velogein.view.auth.form

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.utils.InlineSize
import net.intervallayers.velogein.utils.UserSelect
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import net.intervallayers.velogein.view.component.Separator

/**
 * Форма для отображения логотипа с приветствием во время процесса авторизации.
 *
 * @author Nourepide@gmail.com
 */
class HeaderForm : FlexVerticalLayout() {

    init {
        configureHeader()
    }

    /**
     * Конфигурация шапки с логотипом.
     */
    private fun configureHeader() {
        val image = Image("./images/logo_bright.svg", "logo").apply {
            className = UserSelect.NONE
            maxHeight = "50%"
            maxWidth = "50%"
        }

        val h1 = H1("Византийская литания").apply {
            addClassNames(Margin.NONE, InlineSize.MAX_CONTENT, "no-font-size")
        }

        val h2 = H2("Вход в казначейство").apply {
            addClassNames(Margin.NONE, InlineSize.MAX_CONTENT)
        }

        val limiter = VerticalLayout(h1, Separator(), h2).apply {
            addClassNames(Width.AUTO, AlignItems.CENTER)
        }


        addClassNames(Padding.NONE, Width.AUTO, AlignItems.CENTER)
        add(image, limiter)

    }

}
