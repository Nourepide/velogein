package net.intervallayers.velogein.service

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import org.springframework.stereotype.Service

@Service
class ClientService {

    @Suppress("UsePropertyAccessSyntax")
    fun setClientThemeModeBright() {
        val isDark = UI
            .getCurrent()
            .getElement()
            .getThemeList()
            .contains("dark")

        if (isDark) {
            UI
                .getCurrent()
                .getElement()
                .getThemeList()
                .remove("dark")
        }
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setClientThemeModeDark() {
        val isBright = UI
            .getCurrent()
            .getElement()
            .getThemeList()
            .contains("dark")
            .not()

        if (isBright) {
            UI
                .getCurrent()
                .getElement()
                .getThemeList()
                .add("dark")
        }
    }

    fun navigateTo(view: Class<out Component>) {
        UI
            .getCurrent()
            .navigate(view)


    }

}
