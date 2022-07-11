package net.intervallayers.velogein.service

import com.vaadin.flow.server.VaadinSession
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.ThemeMode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SessionService {

    @Suppress("UsePropertyAccessSyntax")
    fun getSessionThemeMode(): ThemeMode? {
        return VaadinSession
            .getCurrent()
            .getSession()
            .getAttribute(ThemeMode::class.java.name) as ThemeMode?
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setSessionThemeModeBright() {
        VaadinSession
            .getCurrent()
            .getSession()
            .setAttribute(ThemeMode::class.java.name, ThemeMode.BRIGHT)
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setSessionThemeModeDark() {
        VaadinSession
            .getCurrent()
            .getSession()
            .setAttribute(ThemeMode::class.java.name, ThemeMode.DARK)
    }

    @Suppress("UsePropertyAccessSyntax")
    fun getSessionAccountUsername(): String {
        var username = VaadinSession
            .getCurrent()
            .getSession()
            .getAttribute(Account::class.java.name + "." + Account::username.name) as String?

        if (username == null) {
            username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()

            setSessionAccountUsername(username)
        }

        return username!!
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setSessionAccountUsername(username: String) {
        VaadinSession
            .getCurrent()
            .getSession()
            .setAttribute(Account::class.java.name + ".username", username)
    }

}
