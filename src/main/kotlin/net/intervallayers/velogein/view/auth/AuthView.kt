package net.intervallayers.velogein.view.auth

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent.*
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.theme.lumo.LumoUtility
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.utils.createErrorNotification
import net.intervallayers.velogein.utils.createSuccessNotification
import net.intervallayers.velogein.view.auth.form.NewLoginForm
import net.intervallayers.velogein.view.auth.form.RegistrationForm
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.ServletException

/**
 * Главная страница аунтефикации.
 * Появляется если пользователь не авторизован в системе.
 * Позволяет авторизоваться либо зарегистрироваться.
 */
@Route("auth")
@PageTitle("Византийская литания")
class AuthView(var accountService: AccountService) : VerticalLayout(), BeforeEnterObserver {

    private val header = VerticalLayout()
    private var newLoginForm = NewLoginForm()
    private val registrationForm = RegistrationForm()

    init {
        configureContainer()
        configureHeader()
        configureNewLoginForm()
        configureRegistrationForm()

        add(header, newLoginForm)
    }

    /**
     * Конфигурация главного контейнера.
     */
    private fun configureContainer() {
        addClassName("login-view")
        setSizeFull()
        alignItems = Alignment.CENTER
        justifyContentMode = JustifyContentMode.CENTER
    }

    /**
     * Конфигурация шапки с логотипом.
     */
    private fun configureHeader() {
        val logo = Image("./images/logo.svg", "logo").apply {
            maxHeight = "50%"
            maxWidth = "50%"
        }

        val h1 = H1("Византийская литания").apply {
            className = "no-margin inline-size-max-content no-font-size"
        }

        val h2 = H2("Вход в казначейство").apply {
            className = "no-margin inline-size-max-content"
        }

        val separator = Hr().apply {
            with(style) {
                set("height", "2px")
                set("opacity", "25%")
                set("background", "var(--lumo-header-text-color)")
            }
        }

        with(header) {
            add(logo, h1, separator, h2)

            className = "no-padding"
            alignItems = Alignment.CENTER
            width = LumoUtility.Width.AUTO
        }
    }

    /**
     * Подключение события авторизации на форме логина.
     */
    private fun configureNewLoginForm() {
        with(newLoginForm) {
            addListener(NewLoginForm.LoginButtonClickEvent::class.java) {
                try {
                    VaadinServletRequest
                        .getCurrent()
                        .login(getUsername(), getPassword())

                    UI
                        .getCurrent()
                        .navigate("/")

                    createSuccessNotification("Успешная авторизация.")
                } catch (e: ServletException) {
                    createErrorNotification("Ошибка авторизации.")
                }
            }
            addListener(NewLoginForm.RegistrationButtonClickEvent::class.java) {
                registrationForm.open()
                /**
                 * Подключение события авторизации на форме логина.
                 */
            }
        }
    }

    /**
     * Подключение события регистрации на форме регистрации.
     */
    private fun configureRegistrationForm() {
        with(registrationForm) {
            addListener(RegistrationForm.RegistrationButtonClickEvent::class.java) {
                try {
                    accountService.save(account)

                    VaadinServletRequest
                        .getCurrent()
                        .login(account.username, account.passwordDecode)
                        .also { close() }

                    UI
                        .getCurrent()
                        .navigate("/")

                    createSuccessNotification("Успешная регистрация.")
                } catch (e: AccountService.AccountSaveAlreadyExistExceptions) {
                    createErrorNotification("Пользователь с таким именем уже существует.")
                }
            }
        }
    }

    /**
     * Перекидывание не анонимных пользователей со страницы авторизации.
     */
    override fun beforeEnter(event: BeforeEnterEvent) {
        val isSessionActive = SecurityContextHolder.getContext().authentication.principal != "anonymousUser"

        if (isSessionActive) {
            event.forwardTo("")
        }
    }

}
