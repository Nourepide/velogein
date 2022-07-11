package net.intervallayers.velogein.view.layout

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.ThemeMode
import net.intervallayers.velogein.security.SecurityService
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.service.ClientService
import net.intervallayers.velogein.service.SessionService
import net.intervallayers.velogein.utils.Gap
import net.intervallayers.velogein.utils.InlineSize
import net.intervallayers.velogein.utils.UserSelect
import net.intervallayers.velogein.utils.UtilsWidth
import net.intervallayers.velogein.utils.createErrorNotification
import net.intervallayers.velogein.utils.createSuccessNotification
import net.intervallayers.velogein.view.component.FlexHorizontalLayout
import net.intervallayers.velogein.view.component.FlexVerticalLayout
import net.intervallayers.velogein.view.component.Separator
import net.intervallayers.velogein.view.deposit.DepositView
import net.intervallayers.velogein.view.domicile.DomicileView
import net.intervallayers.velogein.view.main.MainView

/**
 * Обёртка с шапкой и боковой панелью для возможности навигации по страницам.
 * Дополнительно поддерживает функционал смены режима оформления аккаунта и выходом из него.
 *
 * @author Nourepide@gmail.com
 */
class MainLayout(
    private val accountService: AccountService,
    private val securityService: SecurityService,
    private val sessionService: SessionService,
    private val clientService: ClientService,
) : AppLayout() {

    private val header = FlexHorizontalLayout()
    private val drawerToggle = DrawerToggle()
    private val logoContainer = FlexHorizontalLayout()
    private val image = Image()
    private val textContainer = FlexVerticalLayout()
    private val textTop = H1("Византийская литания")
    private val textBottom = H1()
    private val addButton = Button(Icon(VaadinIcon.PLUS))
    private val accountButton = Button(Icon(VaadinIcon.USER))
    private val accountForm = AccountForm(accountService)
    private val themeModeChangeButton = Button()
    private val logoutButton = Button()
    private val drawerTextContainer = FlexVerticalLayout()
    private val drawerTabs = Tabs()

    private val routeTabs = mutableMapOf<Class<out Component>, Tab>()

    init {
        configureHeader()
        configureDrawer()
    }

    /**
     * Настраивает шапку. Добавляет в неё кнопку открытие боковой панели,
     * логотип с текстом и кнопку выхода.
     */
    private fun configureHeader() {
        with(drawerToggle) {
            addThemeVariants(ButtonVariant.LUMO_CONTRAST)
        }

        with(textTop) {
            addClassNames(FontSize.SMALL, Margin.NONE, InlineSize.MAX_CONTENT)
        }

        with(textBottom) {
            addClassNames(FontSize.SMALL, Margin.NONE, InlineSize.MAX_CONTENT)
        }

        with(textContainer) {
            addClassNames(Gap.NONE, Padding.SMALL, AlignItems.CENTER, "mobile-hide")
            add(textTop, Separator(), textBottom)
        }

        with(image) {
            addClassNames(Width.XLARGE, Height.XLARGE, UserSelect.NONE)
        }

        with(logoContainer) {
            addClassNames(Margin.Right.AUTO, AlignItems.CENTER)
            add(image, textContainer)
        }

        with(addButton) {
            addThemeVariants(ButtonVariant.LUMO_ICON)
            element.setAttribute("aria-label", "Add item")

            addClickListener {
                createSuccessNotification("Успешное уведомление.")
                createErrorNotification("Полностью не успешное и очень не удачное уведомление с длинным текстом.")
            }

            hideAddButton()
        }

        with(accountButton) {
            addThemeVariants(ButtonVariant.LUMO_ICON)
            element.setAttribute("aria-label", "Add item")

            addClickListener {
                accountForm.open()
            }
        }

        with(themeModeChangeButton) {
            addThemeVariants(ButtonVariant.LUMO_ICON)
            element.setAttribute("aria-label", "Change theme mode")

            addClickListener {
                toggleThemeMode()
            }
        }

        with(logoutButton) {
            icon = Icon(VaadinIcon.ARROW_RIGHT)
            isIconAfterText = true

            addClickListener {
                securityService.logout()
            }
        }

        with(header) {
            addClassNames(Width.FULL, Padding.Vertical.NONE, Padding.Horizontal.MEDIUM, AlignItems.CENTER)
            add(drawerToggle, logoContainer, addButton, accountButton, themeModeChangeButton, logoutButton)
        }

        addToNavbar(true, header)
    }

    /**
     * Настраивает выдвижную боковую панель.
     * Добавляет в неё элементы для перехода на другие страницы.
     */
    private fun configureDrawer() {
        with(routeTabs) {
            this[MainView::class.java] = createTab(
                VaadinIcon.NOTEBOOK, "Казначейство", MainView::class.java
            )
            this[DepositView::class.java] = createTab(
                VaadinIcon.MONEY_DEPOSIT, "Депозиты", DepositView::class.java
            )
            this[DomicileView::class.java] = createTab(
                VaadinIcon.USERS, "Резиденты", DomicileView::class.java
            )
        }

        with(drawerTextContainer) {
            val text = H1("Византийская литания")
            val separator = Separator()

            addClassName("mobile-show")

            addClassNames(AlignItems.CENTER, UtilsWidth.FIT_CONTENT, Margin.Left.AUTO, Margin.Right.AUTO)

            with(text) {
                addClassNames(FontSize.LARGE, Margin.NONE, InlineSize.MAX_CONTENT)
            }

            add(text, separator)
        }

        with(drawerTabs) {
            orientation = Tabs.Orientation.VERTICAL
            routeTabs.values.forEach { add(it) }
        }

        addToDrawer(drawerTextContainer, drawerTabs)
    }

    /**
     * Создаёт элемент для боковой панели.
     */
    private fun createTab(viewIcon: VaadinIcon, viewName: String, navigationTarget: Class<out Component>): Tab {
        val icon: Icon = viewIcon.create()

        with(icon.style) {
            set("box-sizing", "border-box")
            set("margin-inline-end", "var(--lumo-space-m)")
            set("margin-inline-start", "var(--lumo-space-xs)")
            set("padding", "var(--lumo-space-xs)")
        }

        val link = RouterLink().apply {
            add(icon, Span(viewName))
            setRoute(navigationTarget)
            tabIndex = -1
        }

        return Tab(link)
    }

    /**
     * После каждого перехода корректирует надпись в логотипе,
     * а так-же проверят что в боковой панели выбрана корректная вкладка.
     */
    override fun afterNavigation() {
        initializeThemeMode()

        textBottom.text = getCurrentPageTitle()
        drawerTabs.selectedTab = getCurrentViewTab()

        when (content::class) {
            DomicileView::class -> {
                showAddButton()
            }
            else -> hideAddButton()
        }
    }

    /**
     * Позволяет получить имя открытой страницы.
     * Берёт её из аннотации PageTitle.
     *
     *  @see PageTitle
     */
    private fun getCurrentPageTitle(): String {
        val title = content.javaClass.getAnnotation(PageTitle::class.java)
        return title?.value ?: ""
    }

    /**
     * Находит во вкладках ту у которой совпадает тип с открытой страницей.
     */
    private fun getCurrentViewTab(): Tab {
        return routeTabs.getValue(content.javaClass)
    }

    /**
     * Инициализирует режим оформления после загрузки страницы в первый раз.
     * Подхватывает значение из сессии. Если значение не было инициализировано,
     * то корректирует значение сессии.
     */
    private fun initializeThemeMode() {
        var themeMode = sessionService.getSessionThemeMode()

        if (themeMode == null) {
            themeMode = accountService.getThemeMode()
        }

        when (themeMode) {
            ThemeMode.BRIGHT -> {
                sessionService.setSessionThemeModeBright()
                clientService.setClientThemeModeBright()

                setImageBright()
                setThemeModeChangeButtonBright()
            }
            ThemeMode.DARK -> {
                sessionService.setSessionThemeModeDark()
                clientService.setClientThemeModeDark()

                setImageDark()
                setThemeModeChangeButtonDark()
            }
        }
    }

    /**
     * Переключает на альтернативный стиль оформления аккаунта.
     */
    fun toggleThemeMode() {
        when (sessionService.getSessionThemeMode()!!) {
            ThemeMode.BRIGHT -> {
                accountService.setThemeModeDark()
                sessionService.setSessionThemeModeDark()
                clientService.setClientThemeModeDark()

                setImageDark()
                setThemeModeChangeButtonDark()
            }

            ThemeMode.DARK -> {
                accountService.setThemeModeBright()
                sessionService.setSessionThemeModeBright()
                clientService.setClientThemeModeBright()

                setImageBright()
                setThemeModeChangeButtonBright()
            }
        }
    }

    /**
     * Устанавливает изображение логотипа в тёмный режим.
     */
    fun setImageBright() {
        image.src = "./images/logo_bright.svg"
    }

    /**
     * Устанавливает изображение логотипа в светлый режим.
     */
    fun setImageDark() {
        image.src = "./images/logo_dark.svg"
    }

    /**
     * Устанавливает иконку кнопки смены режима в светлый режим.
     */
    fun setThemeModeChangeButtonBright() {
        themeModeChangeButton.icon = Icon(VaadinIcon.MOON_O)
    }

    /**
     * Устанавливает иконку кнопки смены режима в тёмный режим.
     */
    fun setThemeModeChangeButtonDark() {
        themeModeChangeButton.icon = Icon(VaadinIcon.SUN_O)
    }

    fun showAddButton() {
        with(addButton) {
            removeClassName(Display.HIDDEN)
        }
    }

    fun hideAddButton() {
        with(addButton) {
            addClassName(Display.HIDDEN)
        }
    }

}
