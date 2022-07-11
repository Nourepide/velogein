package net.intervallayers.velogein.view.auth

import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.security.SecurityService
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.service.ClientService
import net.intervallayers.velogein.service.SessionService
import net.intervallayers.velogein.utils.createErrorNotification
import net.intervallayers.velogein.utils.createSuccessNotification
import net.intervallayers.velogein.view.auth.form.HeaderForm
import net.intervallayers.velogein.view.auth.form.NewLoginForm
import net.intervallayers.velogein.view.auth.form.RegistrationForm
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import net.intervallayers.velogein.view.main.MainView
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
class AuthView(
    private val accountService: AccountService,
    private val securityService: SecurityService,
    private val sessionService: SessionService,
    private val clientService: ClientService,
) : FlexVerticalLayout(), BeforeEnterObserver {

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
                    if (!accountService.isAccountExistByUsername(getUsername())) {
                        createErrorNotification("Пользователя с таким именем не существует.")
                    } else {
                        with(securityService) {
                            login(getUsername(), getPassword())
                        }

                        with(sessionService) {
                            setSessionAccountUsername(getUsername())
                        }

                        with(clientService) {
                            navigateTo(MainView::class.java)
                        }

                        createSuccessNotification("Успешная авторизация.")
                    }
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
    private fun configureRegistrationForm() {
        with(registrationForm) {
            addListener(RegistrationForm.RegistrationButtonClickEvent::class.java) {
                try {
                    with(accountService) {
                        createAccount(account)
                    }

                    with(securityService) {
                        login(account.username, account.passwordDecode!!)
                    }

                    close()

                    with(sessionService) {
                        setSessionAccountUsername(account.username)
                    }

                    with(clientService) {
                        navigateTo(MainView::class.java)
                    }

                    createSuccessNotification("Успешная регистрация.")
                } catch (e: AccountService.AccountCreateAlreadyExistExceptions) {
                    createErrorNotification("Пользователь с таким именем уже существует.")
                } catch (e: ServletException) {
                    createErrorNotification("Ошибка авторизации после регистрации.")
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
