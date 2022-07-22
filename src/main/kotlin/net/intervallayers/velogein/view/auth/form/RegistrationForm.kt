package net.intervallayers.velogein.view.auth.form

import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.utils.addEnterKeyListener
import net.intervallayers.velogein.utils.bind
import net.intervallayers.velogein.utils.setRequiredNotEmpty
import net.intervallayers.velogein.utils.setRequiredNotEmptyAndEquals
import net.intervallayers.velogein.view.component.FlexDialog
import net.intervallayers.velogein.view.component.FlexPasswordField
import net.intervallayers.velogein.view.component.FlexTextField
import net.intervallayers.velogein.view.layout.AccountForm

/**
 * Форма регистрации сделанная на основе диалога.
 * Все поля имеют проверки на количество символов, а так-же на совпадение полей пароля.
 *
 * @author Nourepide@gmail.com
 */
class RegistrationForm : FlexDialog() {

    companion object {
        private const val USERNAME_FIELD_VALID_SIZE = 1
        private const val PASSWORD_FIELD_VALID_SIZE = 1
    }

    private val usernameField = FlexTextField("Имя пользователя")
    private val passwordField = FlexPasswordField("Пароль")
    private val passwordRepeatField = FlexPasswordField("Повторите пароль")
    private val firstNameField = FlexTextField("Имя")
    private val lastNameField = FlexTextField("Фамилия")

    private val binder: Binder<Account> = BeanValidationBinder(Account::class.java)
    val account: Account by bind(Account.createEmpty()) { binder.readBean(it) }

    init {
        configureContent()
        configureActionButton()
        configureBinder()

        addOpenedChangeListener {
            actionButtonIsEnabledCheck()
        }
    }

    /**
     * Конфигурация основного контентного блока.
     * Так-же конфигурирует: usernameField,passwordField,
     * passwordRepeatField, firstNameField, lastNameField.
     */
    private fun configureContent() {
        with(usernameField) {
            addClassName(Padding.Top.NONE)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredNotEmpty()
            focus()
        }

        with(passwordField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredNotEmpty()
        }

        with(passwordRepeatField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredNotEmptyAndEquals(passwordField)
        }

        with(firstNameField) {
            addEnterKeyListener { actionButtonClickInClient() }
        }

        with(lastNameField) {
            addClassNames(Width.FULL, Padding.Bottom.NONE)
            addEnterKeyListener { actionButtonClickInClient() }
            setId("lastName")
        }

        addToContent(
            usernameField,
            passwordField,
            passwordRepeatField,
            firstNameField,
            lastNameField
        )
    }

    /**
     * Конфигурация кнопки регистрации.
     */
    private fun configureActionButton() {
        actionButtonSetText("Зарегистрироваться")

        beforeActionButtonClickEvent {
            binder.writeBean(account)
        }
    }

    /**
     * Конфигурация binder.
     * Привязывает поля из основного контентного блока к переменной Account.
     *
     * Единственно красивым способом избавится от дубликата кода является
     * создания отдельного класса с проверкой и условием, к сожалению для
     * этого класса и того где наблюдается второй дубликат наследование
     * от классов более невозможно.
     *
     * @see binder
     * @see account
     * @see AccountForm.configureBinder
     */
    @Suppress("DuplicatedCode")
    private fun configureBinder() {
        with(binder) {
            bind(usernameField, Account::username::get, Account::username::set)
            bind(passwordField, Account::passwordDecode::get, Account::passwordDecode::set)
            bind(firstNameField, Account::firstName::get, Account::firstName::set)
            bind(lastNameField, Account::lastName::get, Account::lastName::set)
        }
    }

    /**
     * Проверка на доступность кнопки действия.
     * Проверят что все обязательные поля заполнены,
     * а так-же что поля паролей совпадают.
     */
    private fun actionButtonIsEnabledCheck() {
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

        actionButtonSetIsEnabled(
            usernameFieldIsValid
                    && passwordFieldIsValid
                    && passwordFieldRepeatIsValid
                    && passwordFieldMatch
        )
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

}
