package net.intervallayers.velogein.view.auth.form

import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.shared.Registration
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.utils.AbstractEvent
import net.intervallayers.velogein.utils.addEnterKeyListener
import net.intervallayers.velogein.utils.bind
import net.intervallayers.velogein.utils.setRequiredNotEmpty
import net.intervallayers.velogein.utils.setRequiredNotEmptyAndEquals

/**
 * Форма регистрации сделанная на основе диалога.
 * Необходимо скрывать в случае перехода на другую страницу при открытой форме.
 * Все поля имеют проверки на количество символов, а так-же на совпадение полей пароля.
 *
 * @author Nourepide@gmail.com
 */
class RegistrationForm : Dialog() {

    companion object {
        private const val USERNAME_FIELD_VALID_SIZE = 1
        private const val PASSWORD_FIELD_VALID_SIZE = 1
    }

    private val content = VerticalLayout()
    private val usernameField = TextField("Имя пользователя")
    private val passwordField = PasswordField("Пароль")
    private val passwordRepeatField = PasswordField("Повторите пароль")
    private val firstNameField = TextField("Имя")
    private val lastNameField = TextField("Фамилия")
    private val cancelButton = Button("Закрыть")
    private val registrationButton = Button("Зарегистрироваться")

    private val binder: Binder<Account> = BeanValidationBinder(Account::class.java)
    val account: Account by bind(Account.createEmpty()) { binder.readBean(it) }

    init {
        configureContent()
        configureCancelButton()
        configureRegistrationButton()
        configureBinder()
    }

    /**
     * Конфигурация кнопки закрытия.
     */
    private fun configureCancelButton() {
        with(cancelButton) {
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            addClickListener { close() }
            with(style) {
                set("margin-right", "auto")
            }
        }

        footer.add(cancelButton)
    }

    /**
     * Конфигурация кнопки регистрации.
     */
    private fun configureRegistrationButton() {
        with(registrationButton) {
            addThemeVariants(ButtonVariant.LUMO_PRIMARY)
            addClickListener {
                binder.writeBean(account)
                fireEvent(RegistrationButtonClickEvent(this@RegistrationForm))
            }
        }
        registrationButtonIsEnabledCheck()

        footer.add(registrationButton)
    }

    /**
     * Конфигурация основного контентного блока.
     * Так-же конфигурирует: usernameField,passwordField,
     * passwordRepeatField, firstNameField, lastNameField.
     */
    private fun configureContent() {
        with(usernameField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { registrationButtonIsEnabledCheck() }
            addEnterKeyListener { registrationButton.clickInClient() }
            setRequiredNotEmpty()
            with(style) {
                set("width", "100%")
                set("padding-top", "0")
            }
            focus()
        }

        with(passwordField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { registrationButtonIsEnabledCheck() }
            addEnterKeyListener { registrationButton.clickInClient() }
            setRequiredNotEmpty()
            width = "100%"
        }

        with(passwordRepeatField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { registrationButtonIsEnabledCheck() }
            addEnterKeyListener { registrationButton.clickInClient() }
            setRequiredNotEmptyAndEquals(passwordField)
            width = "100%"
        }

        with(firstNameField) {
            addEnterKeyListener { registrationButton.clickInClient() }
            width = "100%"
        }

        with(lastNameField) {
            addEnterKeyListener { registrationButton.clickInClient() }
            with(style) {
                set("width", "100%")
                set("padding-bottom", "0")
            }
            setId("lastName")
        }

        with(content) {
            className = "no-gap"

            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            alignItems = FlexComponent.Alignment.CENTER

            with(style) {
                set("width", "calc(var(--lumo-size-s) * 10)")
            }

            add(
                usernameField,
                passwordField,
                passwordRepeatField,
                firstNameField,
                lastNameField
            )
        }

        add(content)
    }

    /**
     * Конфигурация binder.
     * Привязывает поля из основного контентного блока к переменной Account.
     *
     * @see binder
     * @see account
     */
    fun configureBinder() {
        with(binder) {
            bind(usernameField, Account::username::get, Account::username::set)
            bind(passwordField, Account::password::get, Account::password::set)
            bind(firstNameField, Account::firstName::get, Account::firstName::set)
            bind(lastNameField, Account::lastName::get, Account::lastName::set)
        }
    }

    /**
     * Проверка на доступность кнопки регистрации.
     * Проверят что все обязательные поля заполнены,
     * а так-же что поля паролей совпадают.
     */
    private fun registrationButtonIsEnabledCheck() {
        val usernameFieldIsValid = getUsername()
            .trim()
            .length >= USERNAME_FIELD_VALID_SIZE

        val passwordFieldIsValid = getPassword()
            .trim()
            .length >= PASSWORD_FIELD_VALID_SIZE

        val passwordFieldRepeatIsValid = getPasswordRepeat()
            .trim()
            .length >= PASSWORD_FIELD_VALID_SIZE

        val passwordFieldMatch = getPassword() == getPasswordRepeat()

        registrationButton.isEnabled = usernameFieldIsValid && passwordFieldIsValid && passwordFieldRepeatIsValid && passwordFieldMatch
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
     * Функция для проброса значения из поля passwordRepeatField.
     * @see passwordRepeatField
     */
    fun getPasswordRepeat(): String {
        return passwordRepeatField.value
    }

    /**
     * Реализация прослушивания событий через стандартный маршрутизатор.
     */
    public override fun <T : ComponentEvent<*>> addListener(eventType: Class<T>, listener: ComponentEventListener<T>): Registration {
        return eventBus.addListener(eventType, listener)
    }

    /**
     * Событие для проброса срабатывающее после нажатие на кнопку регистрации.
     *
     * @see registrationButton
     * @see configureRegistrationButton
     */
    class RegistrationButtonClickEvent(source: RegistrationForm) : AbstractEvent<RegistrationForm>(source)

}
