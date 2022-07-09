package net.intervallayers.velogein.service

import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.ThemeMode
import org.springframework.stereotype.Service

@Service
class VaadinService {

    fun getSessionThemeMode(): ThemeMode? {
        return VaadinSession.getCurrent().session.getAttribute(ThemeMode::class.java.name) as ThemeMode?
    }

    fun setSessionThemeMode(themeMode: ThemeMode, block: ((ThemeMode) -> Unit)? = null) {
        when (themeMode) {
            ThemeMode.BRIGHT -> {
                UI.getCurrent().element.themeList.remove("dark")
                VaadinSession.getCurrent().session.setAttribute(ThemeMode::class.java.name, ThemeMode.BRIGHT)
            }
            ThemeMode.DARK -> {
                UI.getCurrent().element.themeList.add("dark")
                VaadinSession.getCurrent().session.setAttribute(ThemeMode::class.java.name, ThemeMode.DARK)
            }
        }

        if (block != null) {
            block(themeMode)
        }
    }

    @Suppress("UsePropertyAccessSyntax")
    fun getSessionAccountUsername(): String? {
        return VaadinSession
            .getCurrent()
            .getSession()
            .getAttribute(Account::class.java.name + "." + Account::username.name) as String?
    }

}
