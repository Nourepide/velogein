package net.intervallayers.velogein.view.layout

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.Domicile
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.utils.Gap
import net.intervallayers.velogein.utils.addEnterKeyListener
import net.intervallayers.velogein.utils.bind
import net.intervallayers.velogein.utils.createErrorNotification
import net.intervallayers.velogein.utils.setRequiredEquals
import net.intervallayers.velogein.utils.setRequiredNotEmpty
import net.intervallayers.velogein.view.auth.form.RegistrationForm
import net.intervallayers.velogein.view.component.FlexVerticalLayout

class AccountForm(private val accountService: AccountService) : Dialog() {

    companion object {
        private const val USERNAME_FIELD_VALID_SIZE = 1
    }

    private val content = FlexVerticalLayout()
    private val usernameField = TextField("Имя пользователя")
    private val passwordField = PasswordField("Пароль")
    private val passwordRepeatField = PasswordField("Повторите пароль")
    private val firstNameField = TextField("Имя")
    private val lastNameField = TextField("Фамилия")
    private val cancelButton = Button("Закрыть")
    private val saveButton = Button("Сохранить")
    private val domicileBox = ComboBox<Domicile>("Резидент")

    private val binder: Binder<Account> = BeanValidationBinder(Account::class.java)
    private var account: Account by bind(Account.createEmpty()) { binder.readBean(it) }

    init {
        configureContent()
        configureCancelButton()
        configureSaveButton()
        configureBinder()

        addOpenedChangeListener {
            account = accountService.getAccount()
            passwordRepeatField.value = ""

            saveButtonIsEnabledCheck()
        }
    }

    fun configureContent() {
        with(usernameField) {
            addClassNames(Width.FULL, Padding.Top.NONE)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { saveButtonIsEnabledCheck() }
            addEnterKeyListener { saveButton.clickInClient() }
            setRequiredNotEmpty()

        }

        with(passwordField) {
            addClassName(Width.FULL)
            isRequired = true
            placeholder = "(Без изменений)"
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { saveButtonIsEnabledCheck() }
            addEnterKeyListener { saveButton.clickInClient() }
        }

        with(passwordRepeatField) {
            addClassName(Width.FULL)
            isRequired = true
            placeholder = "(Без изменений)"
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { saveButtonIsEnabledCheck() }
            addEnterKeyListener { saveButton.clickInClient() }
            setRequiredEquals(passwordField)
        }

        with(firstNameField) {
            addClassName(Width.FULL)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { saveButtonIsEnabledCheck() }
            addEnterKeyListener { saveButton.clickInClient() }
        }

        with(lastNameField) {
            addClassName(Width.FULL)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { saveButtonIsEnabledCheck() }
            addEnterKeyListener { saveButton.clickInClient() }
        }

        with(domicileBox) {
            addClassNames(Width.FULL, Padding.Bottom.NONE)

            if (isEmpty) {
                isEnabled = false
            }
        }

        with(content) {
            addClassNames(Gap.NONE, AlignItems.CENTER, JustifyContent.CENTER)

            with(style) {
                set("width", "calc(var(--lumo-size-s) * 10)")
            }

            add(
                usernameField,
                passwordField,
                passwordRepeatField,
                firstNameField,
                lastNameField,
                domicileBox
            )
        }

        add(content)
    }

    fun configureCancelButton() {
        with(cancelButton) {
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            addClickListener { close() }
            addClassName(Margin.Right.AUTO)
        }

        footer.add(cancelButton)
    }

    fun configureSaveButton() {
        with(saveButton) {
            addThemeVariants(ButtonVariant.LUMO_PRIMARY)
            addClickListener {
                if (account.username != usernameField.value) {
                    account.isUsernameChanged = true
                }

                if (account.isUsernameChanged && accountService.isAccountExistByUsername(usernameField.value)) {
                    createErrorNotification("Аккаунт с таким именем уже существует.")
                } else {
                    binder.writeBean(account)
                    account.bCryptEncode()
                    accountService.updateAccount(account.bCryptEncode())

                    close()
                }
            }
        }

        footer.add(saveButton)
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
        }
    }

    /**
     * Проверка на доступность кнопки регистрации.
     * Проверят что все обязательные поля заполнены,
     * а так-же что поля паролей совпадают.
     */
    private fun saveButtonIsEnabledCheck() {
        val usernameFieldIsValid = account.username
            .trim()
            .length >= USERNAME_FIELD_VALID_SIZE

        val passwordFieldMatch = passwordField.value == passwordRepeatField.value

        val isChanged = usernameField.value != account.username
                || passwordField.value.isNotEmpty()
                || firstNameField.value != account.firstName
                || lastNameField.value != account.lastName

        saveButton.isEnabled = usernameFieldIsValid && passwordFieldMatch && isChanged
    }

}
