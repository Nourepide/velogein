package net.intervallayers.velogein.view.auth.form

import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.utils.AbstractEvent
import net.intervallayers.velogein.utils.Gap
import net.intervallayers.velogein.utils.addEnterKeyListener
import net.intervallayers.velogein.utils.setRequiredNotEmpty
import net.intervallayers.velogein.view.component.FlexPasswordField
import net.intervallayers.velogein.view.component.FlexTextField
import net.intervallayers.velogein.view.component.FlexVerticalLayout

/**
 * Форма авторизации созданная с целью заменить стандартную LoginForm.
 * Из-за того что стандартная форма логина не поддерживает любые изменения,
 * кроме отключения кнопки "Забыли пароль", пришлось с нуля воссоздать всю форму.
 *
 * Новая форма полностью подражает стандартной форме, за исключением:
 * Отсутствует заголовок с текстом "Авторизация",
 * Добавленна проверка на активацию кнопки логина только после заполнения полей,
 * Возможность активировать иконки на кнопках.
 *
 * @see LoginForm
 * @author Nourepide@gmail.com
 */
@Suppress("SpellCheckingInspection")
class NewLoginForm : FlexVerticalLayout() {

    companion object {
        private const val USERNAME_FIELD_VALID_SIZE = 1
        private const val PASSWORD_FIELD_VALID_SIZE = 1
    }

    private val usernameField = FlexTextField("Имя пользователя")
    private val passwordField = FlexPasswordField("Пароль")
    private val loginButton = Button("Войти")
    private val registrationButton = Button("Регистрация")
    private var isIconEnabled = false

    init {
        enableIcon(false)

        configureContainer()
        configureUsernameField()
        configurePasswordField()
        configureLoginButton()
        configureRegistrationButton()

        add(usernameField, passwordField, loginButton, registrationButton)
    }

    /**
     * Конфигурация главного контейнера.
     * Отлючает в нём отступы между элементами и размещает их вертикально по центру.
     */
    private fun configureContainer() {
        addClassNames(Gap.NONE, Padding.LARGE, AlignItems.CENTER)
        with(style) {
            set("width", "calc(var(--lumo-size-m) * 10)")
        }
    }

    /**
     * Конфигурация текстокого поля username.
     * Активирует на каждый ввод текста проверку на доступность кнопки входа.
     * Подключено требование на заполнение.
     * Захватывает фокус ввода после загрузки страницы.
     *
     * @see loginButtonIsEnabledCheck
     * @see setRequiredNotEmpty
     */
    private fun configureUsernameField() {
        with(usernameField) {
            addInputListener { loginButtonIsEnabledCheck() }
            addEnterKeyListener { loginButton.clickInClient() }
            setRequiredNotEmpty(USERNAME_FIELD_VALID_SIZE)

            if (isIconEnabled) {
                prefixComponent = VaadinIcon.USER.create()
            }

            valueChangeMode = ValueChangeMode.EAGER

            focus()
        }
    }

    /**
     * Конфигурация текстокого поля password.
     * Активирует на каждый ввод текста проверку на доступность кнопки входа.
     * Подключено требование на заполнение.
     *
     * @see loginButtonIsEnabledCheck
     * @see setRequiredNotEmpty
     */
    private fun configurePasswordField() {
        with(passwordField) {
            addInputListener { loginButtonIsEnabledCheck() }
            addEnterKeyListener { loginButton.clickInClient() }
            setRequiredNotEmpty(PASSWORD_FIELD_VALID_SIZE)

            if (isIconEnabled) {
                prefixComponent = VaadinIcon.PASSWORD.create()
            }

            valueChangeMode = ValueChangeMode.EAGER
        }
    }

    /**
     * Конфигурация кнопки входа.
     * Привязывает LoginButtonClickEvent к прослушивателю ClickEvent.
     *
     * @see LoginButtonClickEvent
     */
    private fun configureLoginButton() {
        with(loginButton) {
            addClassNames(Width.FULL, Margin.Top.LARGE, Margin.Bottom.SMALL)
            addThemeVariants(ButtonVariant.LUMO_PRIMARY)
            addClickListener { fireEvent(LoginButtonClickEvent(this@NewLoginForm)) }
            loginButtonIsEnabledCheck()
        }
    }

    /**
     * Конфигурация кнопки регистрации.
     * Привязывает RegistrationButtonClickEvent к прослушивателю ClickEvent.
     *
     * @see RegistrationButtonClickEvent
     */
    private fun configureRegistrationButton() {
        with(registrationButton) {
            addClassName(Margin.Top.SMALL)
            addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)
            addClickListener { fireEvent(RegistrationButtonClickEvent(this@NewLoginForm)) }
        }
    }

    /**
     * Проверяет доступность кнопки авторизации.
     * Если оба поля username и password не пустые, то разблокирует кнопку.
     */
    private fun loginButtonIsEnabledCheck() {
        val usernameFieldIsValid = getUsername()
            .trim()
            .length >= USERNAME_FIELD_VALID_SIZE

        val passwordFieldIsValid = getPassword()
            .trim()
            .length >= PASSWORD_FIELD_VALID_SIZE

        loginButton.isEnabled = usernameFieldIsValid && passwordFieldIsValid
    }

    /**
     * Позволяет переключить режим отображения иконок в текстовых полях.
     */
    fun enableIcon(isIconEnabled: Boolean) {
        this.isIconEnabled = isIconEnabled
    }

    /**
     * Функция для проброса значения из поля usernameField.
     * @see usernameField
     */
    fun getUsername(): String {
        return usernameField.value
    }

    /**
     * Функция для проброса значения из поля passwordField.
     * @see passwordField
     */
    fun getPassword(): String {
        return passwordField.value
    }

    /**
     * Реализация прослушивания событий через стандартный маршрутизатор.
     */
    public override fun <T : ComponentEvent<*>> addListener(eventType: Class<T>, listener: ComponentEventListener<T>): Registration {
        return eventBus.addListener(eventType, listener)
    }

    /**
     * Событие для проброса срабатывающее после нажатие на кнопку авторизации.
     *
     * @see loginButton
     * @see configureLoginButton
     */
    class LoginButtonClickEvent(source: NewLoginForm) : AbstractEvent<NewLoginForm>(source)

    /**
     * Событие для проброса срабатывающее после нажатие на кнопку регистрации.
     *
     * @see registrationButton
     * @see configureRegistrationButton
     */
    class RegistrationButtonClickEvent(source: NewLoginForm) : AbstractEvent<NewLoginForm>(source)

}
