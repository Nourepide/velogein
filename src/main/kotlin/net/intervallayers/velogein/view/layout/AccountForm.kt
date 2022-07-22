package net.intervallayers.velogein.view.layout

import com.vaadin.flow.component.ItemLabelGenerator
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.Domicile
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.service.DomicileService
import net.intervallayers.velogein.service.SessionService
import net.intervallayers.velogein.utils.addEnterKeyListener
import net.intervallayers.velogein.utils.bind
import net.intervallayers.velogein.utils.setRequiredEquals
import net.intervallayers.velogein.utils.setRequiredNotEmpty
import net.intervallayers.velogein.view.auth.form.RegistrationForm
import net.intervallayers.velogein.view.component.FlexDialog
import net.intervallayers.velogein.view.component.FlexHorizontalLayout
import net.intervallayers.velogein.view.component.FlexPasswordField
import net.intervallayers.velogein.view.component.FlexTextField
import net.intervallayers.velogein.view.component.StateButton

/**
 * Форма создания и изменения резидента сделанная на основе диалога.
 * Все поля имеют проверки на количество символов, а так-же на возможность изменения.
 * Имеет функционал создания и привязки резидента.
 *
 * @author Nourepide@gmail.com
 */
class AccountForm(
    private val accountService: AccountService,
    private val domicileService: DomicileService,
    private val sessionService: SessionService
) : FlexDialog() {

    companion object {
        private const val USERNAME_FIELD_VALID_SIZE = 1
    }

    private val usernameField = FlexTextField("Имя пользователя")
    private val passwordField = FlexPasswordField("Пароль")
    private val passwordRepeatField = FlexPasswordField("Повторите пароль")
    private val firstNameField = FlexTextField("Имя")
    private val lastNameField = FlexTextField("Фамилия")
    private val domicileContainer = FlexHorizontalLayout()
    private val domicileField = ComboBox<Domicile>("Резидент")
    private val domicileCreateButton = StateButton()

    private val domicileReplacementValue = Domicile(null, "", "")
    var domicileTemporaryValue: Domicile? = null

    private val binder: Binder<Account> = BeanValidationBinder(Account::class.java)
    var account: Account by bind(Account.createEmpty()) { binder.readBean(it) }

    init {
        configureContent()
        configureActionButton()

        addOpenedChangeListener {
            if (it.isOpened) {
                val domiciles = domicileService.findAllWithoutAccountExcept(sessionService.getSessionAccountUsername())

                domicileField.setItems(domiciles)
                account = accountService.getAccount()

                actionButtonIsEnabledCheck()
                domicileCreateButtonIsEnabledCheck()
            } else {
                if (domicileCreateButton.isPressed) {
                    domicileCreateButton.toggleIsPressed()
                }
            }
        }

        configureBinder()
    }

    /**
     * Конфигурация основного контентного блока.
     */
    private fun configureContent() {
        with(usernameField) {
            addClassName(Padding.Top.NONE)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredNotEmpty()

        }

        with(passwordField) {
            isRequired = true
            placeholder = "(Без изменений)"
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
        }

        with(passwordRepeatField) {
            isRequired = true
            placeholder = "(Без изменений)"
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredEquals(passwordField)
        }

        @Suppress("DuplicatedCode")
        with(firstNameField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener {
                actionButtonIsEnabledCheck()
                domicileCreateButtonIsEnabledCheck()

                /**
                 * Костыль, текст в поле резидента изменяется только если заменить самого резидента.
                 */
                if (domicileField.value == domicileReplacementValue) {
                    domicileReplacementValue.firstName = value
                    domicileField.value = domicileReplacementValue
                }
            }
            addEnterKeyListener { actionButtonClickInClient() }
        }

        @Suppress("DuplicatedCode")
        with(lastNameField) {
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener {
                actionButtonIsEnabledCheck()
                domicileCreateButtonIsEnabledCheck()

                /**
                 * Костыль, текст в поле резидента изменяется только если заменить самого резидента.
                 */
                if (domicileField.value == domicileReplacementValue) {
                    domicileReplacementValue.lastName = value
                    domicileField.value = domicileReplacementValue
                }
            }
            addEnterKeyListener { actionButtonClickInClient() }
        }

        with(domicileField) {
            addClassNames(Width.FULL, Padding.Bottom.NONE)
            itemLabelGenerator = ItemLabelGenerator { it.firstName + " " + it.lastName }
            isClearButtonVisible = true

            addValueChangeListener {
                actionButtonIsEnabledCheck()
                domicileCreateButtonIsEnabledCheck()
            }
        }

        /**
         * При нажатии на кнопку создания, основное окно блокируется,
         * и в поле значение подменяется на временное, старое поле переносится во временное значение.
         */
        with(domicileCreateButton) {
            setImageIfUnpressed(Icon(VaadinIcon.PLUS))
            setImageIfPressed(Icon(VaadinIcon.CLOSE_SMALL))

            addClickListener {
                when (domicileCreateButton.isPressed) {
                    true -> {
                        with(domicileReplacementValue) {
                            firstName = firstNameField.value
                            lastName = lastNameField.value
                        }

                        with(domicileField) {
                            domicileTemporaryValue = value
                            value = domicileReplacementValue
                        }
                    }
                    false -> {
                        with(domicileField) {
                            value = domicileTemporaryValue
                        }
                    }
                }
            }
        }

        with(domicileContainer) {
            addClassNames(Width.FULL, AlignItems.BASELINE)
            add(domicileField, domicileCreateButton)
        }

        addToContent(
            usernameField,
            passwordField,
            passwordRepeatField,
            firstNameField,
            lastNameField,
            domicileContainer
        )
    }

    /**
     * Конфигурация кнопки действия.
     */
    private fun configureActionButton() {
        actionButtonSetText("Сохранить")

        beforeActionButtonClickEvent {
            if (account.username != usernameField.value) {
                account.isUsernameChanged = true
            }

            binder.writeBean(account)
        }
    }

    /**
     * Единственно красивым способом избавится от дубликата кода является
     * создания отдельного класса с проверкой и условием, к сожалению для
     * этого класса и того где наблюдается второй дубликат наследование
     * от классов более невозможно.
     *
     * @see RegistrationForm.configureBinder
     */
    @Suppress("DuplicatedCode")
    fun configureBinder() {
        with(binder) {
            bind(usernameField, Account::username::get, Account::username::set)
            bind(passwordField, Account::passwordDecode::get, Account::passwordDecode::set)
            bind(firstNameField, Account::firstName::get, Account::firstName::set)
            bind(lastNameField, Account::lastName::get, Account::lastName::set)
            bind(domicileField, Account::domicile::get, Account::domicile::set)
        }
    }

    /**
     * Проверка на доступность кнопки сохранения.
     * Проверят что все обязательные поля заполнены, поля паролей совпадают и поля изменены.
     */
    private fun actionButtonIsEnabledCheck() {
        val usernameFieldIsValid = account.username
            .trim()
            .length >= USERNAME_FIELD_VALID_SIZE

        val passwordFieldMatch = passwordField.value == passwordRepeatField.value

        val isChanged = usernameField.value != account.username
                || passwordField.value.isNotEmpty()
                || firstNameField.value != account.firstName
                || lastNameField.value != account.lastName
                || domicileField.value != account.domicile

        val additionalFieldsIsValid = if (domicileCreateButton.isPressed) firstNameField.value.isNotEmpty() && lastNameField.value.isNotEmpty() else true

        actionButtonSetIsEnabled(
            usernameFieldIsValid
                    && passwordFieldMatch
                    && isChanged
                    && additionalFieldsIsValid
        )
    }

    /**
     * Проверка на доступность кнопки создания резидента.
     * Проверяет что все поля заполнены.
     */
    private fun domicileCreateButtonIsEnabledCheck() {
        val firstNameFieldIsValid = firstNameField.value.isNotBlank()
        val lastNameFieldIsValid = lastNameField.value.isNotBlank()

        domicileField.isEnabled = !domicileCreateButton.isPressed
        domicileCreateButton.isEnabled = firstNameFieldIsValid && lastNameFieldIsValid
    }

    /**
     * Функция для проброса значения из поля usernameField.
     * @see usernameField
     */
    fun getUsername(): String {
        return usernameField.value
    }

    /**
     * Функция для проброса значения из поля firstNameField.
     * @see firstNameField
     */
    fun getFirstname(): String {
        return firstNameField.value
    }

    /**
     * Функция для проброса значения из поля lastNameField.
     * @see lastNameField
     */
    fun getLastname(): String {
        return lastNameField.value
    }

    /**
     * Функция для проброса значения, что аккаунт был измененён.
     *
     * @see Account.isUsernameChanged
     */
    fun getUsernameIsChanged(): Boolean {
        return account.isUsernameChanged
    }

    /**
     * Функция для проброса значения, что кнопка создания резидента была нажата
     */
    fun getDomicileCreateButtonIsPressed(): Boolean {
        return domicileCreateButton.isPressed
    }

}
