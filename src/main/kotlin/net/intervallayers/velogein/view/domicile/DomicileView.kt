package net.intervallayers.velogein.view.domicile

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.function.SerializableBiConsumer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.Domicile
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.service.DomicileService
import net.intervallayers.velogein.utils.createErrorNotification
import net.intervallayers.velogein.utils.createSuccessNotification
import net.intervallayers.velogein.view.component.FlexDialog
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import net.intervallayers.velogein.view.layout.MainLayout
import javax.annotation.security.RolesAllowed

/**
 * Страница резидентов.
 * Отображает таблицу резидентов, позволяет при нажатии удалить или обновить резидента.
 *
 * @author Nourepide@gmail.com
 */
@RolesAllowed("USER")
@PageTitle("Резиденты")
@Route(value = "domicile", layout = MainLayout::class)
class DomicileView(private val domicileService: DomicileService, accountService: AccountService) : FlexVerticalLayout() {

    private val grid = Grid(Domicile::class.java, false)
    val domicileForm = DomicileForm(accountService)

    init {
        configureContent()
        configureGrid()
        configureDomicileForm()
    }

    /**
     * Конфигурация основного контентного блока.
     */
    private fun configureContent() {
        addClassNames(Width.FULL, Height.FULL)
    }

    /**
     * Конфигурация таблицы резидентов.
     * Добавляет основные поля и вызывает при нажатии форму резидента.
     *
     * @see domicileForm
     */
    private fun configureGrid() {
        with(grid) {
            addClassNames(Width.FULL, Height.FULL, Border.NONE)

            addColumn(Domicile::firstName).setHeader("Имя")
            addColumn(Domicile::lastName).setHeader("Фамилия")
            addColumn(createStatusComponentRenderer()).setHeader("Привязка")

            asSingleSelect().addValueChangeListener {
                if (it.value != null) {
                    deselectAll()

                    domicileForm.domicile = it.value
                    domicileForm.open()
                }
            }

            updateGrid()
        }

        add(grid)
    }

    /**
     * Конфигурация формы резидента.
     * Обрабатывает нажатия на кнопку удаления и кнопку действия
     */
    private fun configureDomicileForm() {
        with(domicileForm) {
            addListener(DomicileForm.DeleteButtonClickEvent::class.java) {
                try {
                    with(domicileService) {
                        deleteDomicile(domicile)
                    }

                    close()
                    updateGrid()
                    createSuccessNotification("Резидент успешно удалён.")
                } catch (e: RuntimeException) {
                    createErrorNotification("Ошибка удаления резидента.")
                }


            }
            addListener(FlexDialog.ActionButtonClickEvent::class.java) {
                try {
                    with(domicileService) {
                        createDomicile(domicile)
                    }

                    close()
                    updateGrid()

                    if (isCreatingMode) {
                        createSuccessNotification("Резидент успешно создан.")
                    } else {
                        createSuccessNotification("Резидент успешно обновлён.")
                    }
                } catch (e: DomicileService.DomicileAlreadyExistException) {
                    createErrorNotification(e.message!!)
                }
            }
        }
    }

    /**
     * Конфигурация бейджа привязки в таблице резидентов.
     */
    private fun createStatusComponentRenderer(): ComponentRenderer<Span?, Domicile?> {
        return ComponentRenderer({ Span() }, SerializableBiConsumer<Span, Domicile> { span, domicile ->
            val link = if (domicile.account == null) "Внешний" else domicile.account!!.username
            val theme = if (domicile.account == null) "badge primary" else "badge success primary"
            with(span) {
                text = link
                element.themeList.add(theme)
            }
        })
    }

    /**
     * Обновление таблицы.
     * Желательно вызывать после каждого изменения резидентов.
     */
    fun updateGrid() {
        grid.setItems(domicileService.findAll())
    }

}
