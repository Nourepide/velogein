package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.utils.AbstractEvent
import net.intervallayers.velogein.utils.Gap

/**
 * Обёртка над диалогом призванная уменьшить количество повторяемого кода.
 * Имеет заранее установленные кнопки закрытия и действия.
 *
 * @author Nourepide@gmail.com
 */
open class FlexDialog : Dialog() {

    private val content = FlexVerticalLayout()
    private val footerAdditional = FlexHorizontalLayout()
    private val cancelButton = Button("Закрыть")
    private val actionButton = Button("Создать")

    private var beforeActionButtonClickEvent: (() -> Unit)? = {}

    init {
        configureContent()
        configureCloseButton()
        configureFooterContent()
        configureActionButton()
    }

    /**
     * Конфигурация основного контентного блока.
     */
    private fun configureContent() {
        with(content) {
            addClassNames(Gap.NONE, AlignItems.START, JustifyContent.CENTER)

            with(style) {
                set("width", "calc(var(--lumo-size-s) * 10)")
            }
        }

        add(content)
    }

    /**
     * Конфигурация кнопки закрытия.
     */
    private fun configureCloseButton() {
        with(cancelButton) {
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            addClickListener { close() }
            addClassName(Margin.Right.AUTO)
        }

        footer.add(cancelButton)
    }

    /**
     * Конфигурация контента между кнопкой закрытия и кнопкой действия.
     */
    private fun configureFooterContent() {
        footer.add(footerAdditional)
    }

    /**
     * Конфигурация кнопки действия.
     * При нажатии выполняет beforeActionButtonClickEvent и пробрасывает событие ActionButtonClickEvent.
     *
     * @see beforeActionButtonClickEvent
     * @see ActionButtonClickEvent
     */
    private fun configureActionButton() {
        with(actionButton) {
            addThemeVariants(ButtonVariant.LUMO_PRIMARY)
            addClickListener {
                if (beforeActionButtonClickEvent != null) {
                    beforeActionButtonClickEvent!!()
                }
                fireEvent(ActionButtonClickEvent(this@FlexDialog))
            }
        }

        footer.add(actionButton)
    }

    /**
     * Метод помощник позволяющий добавить компоненты в контент.
     */
    fun addToContent(vararg components: Component) {
        content.add(*components)
    }

    /**
     * Метод помощник позволяющий добавить компоненты между кнопками закрытия и действия.
     */
    fun addToFooter(vararg components: Component) {
        footerAdditional.add(*components)
    }

    /**
     * Метод помощник позволяющий изменить текст кнопки действия.
     */
    fun actionButtonSetText(text: String) {
        actionButton.text = text
    }

    /**
     * Метод помощник позволяющий установить доступность кнопку действия.
     */
    fun actionButtonSetIsEnabled(isEnabled: Boolean) {
        actionButton.isEnabled = isEnabled
    }

    /**
     * Метод помощник позволяющий инициировать нажатие кнопки действия со стороны клиента.
     */
    fun actionButtonClickInClient() {
        actionButton.clickInClient()
    }

    /**
     * Метод помощник позволяющий добавить процедуру, которая будет происходить перед нажатием кнопки действия.
     */
    fun beforeActionButtonClickEvent(block: () -> Unit) {
        beforeActionButtonClickEvent = block
    }

    /**
     * Реализация прослушивания событий через стандартный маршрутизатор.
     */
    public override fun <T : ComponentEvent<*>> addListener(eventType: Class<T>, listener: ComponentEventListener<T>): Registration {
        return eventBus.addListener(eventType, listener)
    }

    /**
     * Событие для проброса срабатывающее после нажатие на кнопку действия.
     *
     * @see actionButton
     */
    class ActionButtonClickEvent(source: FlexDialog) : AbstractEvent<FlexDialog>(source)

}
