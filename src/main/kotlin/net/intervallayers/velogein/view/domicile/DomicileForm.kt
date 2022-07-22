package net.intervallayers.velogein.view.domicile

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.Domicile
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.utils.AbstractEvent
import net.intervallayers.velogein.utils.addEnterKeyListener
import net.intervallayers.velogein.utils.bind
import net.intervallayers.velogein.utils.setRequiredNotEmpty
import net.intervallayers.velogein.view.component.FlexDialog
import net.intervallayers.velogein.view.component.FlexTextField

/**
 * Форма создания и изменения резидента сделанная на основе диалога.
 * Все поля имеют проверки на количество символов, а так-же на возможность изменения.
 * Имеет режим создания и изменения, определяется автоматически.
 * В случае если изменяется резидент уже привязанный к чужому аккаунту,
 * возможность изменения и удаления блокируется.
 *
 * @author Nourepide@gmail.com
 */
class DomicileForm(private val accountService: AccountService) : FlexDialog() {

    private val firstNameField = FlexTextField("Имя")
    private val lastNameField = FlexTextField("Фамилия")
    private val deleteButton = Button(Icon(VaadinIcon.TRASH))

    private val binder: Binder<Domicile> = BeanValidationBinder(Domicile::class.java)
    var domicile: Domicile by bind(Domicile.createEmpty()) { binder.readBean(it) }
    private lateinit var account: Account
    var isCreatingMode = false

    init {
        configureContent()
        configureDeleteButton()
        configureActionButton()
        configureBinder()

        addOpenedChangeListener {
            if (it.isOpened) {
                account = accountService.getAccount()

                setMode()

                if (isCreatingMode) {
                    actionButtonSetText("Создать")
                } else {
                    actionButtonSetText("Сохранить")
                }

                isLinkedCheck()
                actionButtonIsEnabledCheck()
            }
        }
    }

    /**
     * Конфигурация основного контентного блока.
     */
    private fun configureContent() {
        with(firstNameField) {
            addClassName(Padding.Top.NONE)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredNotEmpty()
        }

        with(lastNameField) {
            addClassName(Padding.Bottom.NONE)
            valueChangeMode = ValueChangeMode.EAGER
            addInputListener { actionButtonIsEnabledCheck() }
            addEnterKeyListener { actionButtonClickInClient() }
            setRequiredNotEmpty()
        }

        addToContent(firstNameField, lastNameField)

    }

    /**
     * Конфигурация кнопки удаления.
     * Устанавливает опасный режим и прокидывает DeleteButtonClickEvent при нажатии.
     */
    fun configureDeleteButton() {
        with(deleteButton) {
            addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR)
            addClickListener {
                fireEvent(DeleteButtonClickEvent(this@DomicileForm))
            }
        }

        addToFooter(deleteButton)
    }

    /**
     * Конфигурация кнопки действия.
     */
    private fun configureActionButton() {
        beforeActionButtonClickEvent {
            binder.writeBean(domicile)
        }
    }

    /**
     * Конфигурация binder.
     * Привязывает поля из основного контентного блока к переменной Domicile.
     */
    private fun configureBinder() {
        with(binder) {
            bind(firstNameField, Domicile::firstName::get, Domicile::firstName::set)
            bind(lastNameField, Domicile::lastName::get, Domicile::lastName::set)
        }
    }

    /**
     * Проверка на доступность кнопки действия.
     * Проверят что все обязательные поля заполнены и изменены.
     */
    private fun actionButtonIsEnabledCheck() {
        val isNotEmpty = firstNameField.value.isNotBlank() && lastNameField.value.isNotBlank()
        val isChanged = firstNameField.value != domicile.firstName || lastNameField.value != domicile.lastName

        actionButtonSetIsEnabled(isNotEmpty && isChanged)
    }

    /**
     * Проверка, что изменяемый резидент не привязан к чужому аккаунту.
     */
    private fun isLinkedCheck() {
        var isNotLinked = true

        if (domicile.account != null) {
            if (domicile.account!!.id != account.id) {
                isNotLinked = false
            }
        }

        deleteButton.isEnabled = isNotLinked
        firstNameField.isReadOnly = !isNotLinked
        lastNameField.isReadOnly = !isNotLinked
    }

    /**
     * Определения текущего режима - создание или модификация.
     */
    fun setMode() {
        isCreatingMode = domicile.id == null
        deleteButton.isVisible = !isCreatingMode
    }

    /**
     * Событие для проброса срабатывающее после нажатие на кнопку удаления резидента.
     *
     * @see actionButton
     */
    class DeleteButtonClickEvent(source: FlexDialog) : AbstractEvent<FlexDialog>(source)

}
