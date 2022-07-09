package net.intervallayers.velogein.view.auth

import com.vaadin.flow.component.UI
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinSession
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.utils.createErrorNotification
import net.intervallayers.velogein.utils.createSuccessNotification
import net.intervallayers.velogein.view.auth.form.HeaderForm
import net.intervallayers.velogein.view.auth.form.NewLoginForm
import net.intervallayers.velogein.view.auth.form.RegistrationForm
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.ServletException

/**
 * Главная страница аунтефикации.
 * Появляется если пользователь не авторизован в системе.
 * Позволяет авторизоваться либо зарегистрироваться.
 *
 * @author Nourepide@gmail.com
 */
@Route("auth")
@PageTitle("Византийская литания")
class AuthView(var accountService: AccountService) : FlexVerticalLayout(), BeforeEnterObserver {

    private val headerForm = HeaderForm()
    private var newLoginForm = NewLoginForm()
    private val registrationForm = RegistrationForm()

    init {
        configureContainer()
        configureNewLoginForm()
        configureRegistrationForm()

        add(headerForm, newLoginForm)
    }

    /**
     * Конфигурация главного контейнера.
     */
    private fun configureContainer() {
        addClassNames(AlignItems.CENTER, JustifyContent.CENTER)
        setSizeFull()
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

                    VaadinSession
                        .getCurrent()
                        .session
                        .setAttribute(Account::class.java.name + ".username", getUsername())

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
            }
        }
    }

    /**
     * Подключение события регистрации на форме регистрации.
     */
    @Suppress("UsePropertyAccessSyntax")
    private fun configureRegistrationForm() {
        with(registrationForm) {
            addListener(RegistrationForm.RegistrationButtonClickEvent::class.java) {
                try {
                    accountService.save(account)

                    VaadinServletRequest
                        .getCurrent()
                        .login(account.username, account.passwordDecode)
                        .also { close() }

                    VaadinSession
                        .getCurrent()
                        .getSession()
                        .setAttribute(Account::class.java.name + ".username", account.username)

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
