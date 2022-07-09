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
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.theme.lumo.LumoUtility.*
import net.intervallayers.velogein.model.ThemeMode
import net.intervallayers.velogein.service.AccountService
import net.intervallayers.velogein.service.VaadinService
import net.intervallayers.velogein.utils.Gap
import net.intervallayers.velogein.utils.InlineSize
import net.intervallayers.velogein.utils.UserSelect
import net.intervallayers.velogein.view.account.AccountView
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
class MainLayout(var accountService: AccountService, var vaadinService: VaadinService) : AppLayout(), BeforeEnterObserver {

    private val header = FlexHorizontalLayout()
    private val drawerToggle = DrawerToggle()
    private val logoContainer = FlexHorizontalLayout()
    private val image = Image()
    private val textContainer = FlexVerticalLayout()
    private val textTop = H1("Византийская литания")
    private val textBottom = H1()
    private val themeModeChangeButton = Button()
    private val logoutButton = Button("Выйти")
    private val tabs = Tabs()

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
            addClassNames(Gap.NONE, Padding.SMALL, AlignItems.CENTER)
            add(textTop, Separator(), textBottom)
        }

        with(image) {
            addClassNames(Width.XLARGE, Height.XLARGE, UserSelect.NONE)
        }

        with(logoContainer) {
            addClassNames(Margin.Right.AUTO, AlignItems.CENTER)
            add(image, textContainer)
        }

        with(logoutButton) {
            addClickListener {
                VaadinServletRequest.getCurrent().logout()
            }
        }

        with(themeModeChangeButton) {
            addThemeVariants(ButtonVariant.LUMO_ICON)
            element.setAttribute("aria-label", "Add item")

            addClickListener {
                toggleAccountThemeMode()
            }
        }

        with(header) {
            addClassNames(Width.FULL, Padding.Vertical.NONE, Padding.Horizontal.MEDIUM, AlignItems.CENTER)
            add(drawerToggle, logoContainer, themeModeChangeButton, logoutButton)
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
            this[AccountView::class.java] = createTab(
                VaadinIcon.USER, "Аккаунт", AccountView::class.java
            )
        }

        with(tabs) {
            orientation = Tabs.Orientation.VERTICAL
            routeTabs.values.forEach { add(it) }
        }

        addToDrawer(tabs)
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
        textBottom.text = getCurrentPageTitle()
        tabs.selectedTab = getCurrentViewTab()
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
     * Ранняя инициализация до полной загрузки страницы.
     */
    override fun beforeEnter(event: BeforeEnterEvent?) {
        initializeThemeMode()
    }

    /**
     * Инициализирует режим оформления после загрузки страницы в первый раз.
     * Подхватывает значение из сессии. Если значение не было инициализировано,
     * то корректирует значение сессии.
     */
    private fun initializeThemeMode() {
        if (vaadinService.getSessionThemeMode() == null) {
            val username = vaadinService.getSessionAccountUsername()

            if (username == null) {
                throw RuntimeException("Имя пользователя в сессии не найдено или неверно.")
            } else {
                setClientThemeMode(accountService.getThemeModeByUsername(username))
            }
        } else {
            setClientThemeMode(vaadinService.getSessionThemeMode()!!)
        }
    }

    /**
     * Включает режим оформления без изменения в аккаунте.
     */
    fun setClientThemeMode(themeMode: ThemeMode) {
        vaadinService.setSessionThemeMode(themeMode) {
            when (themeMode) {
                ThemeMode.BRIGHT -> {
                    setImageBright()
                    setThemeModeChangeButtonBright()
                }
                ThemeMode.DARK -> {
                    setImageDark()
                    setThemeModeChangeButtonDark()
                }
            }
        }
    }

    /**
     * Переключает на альтернативный стиль оформления аккаунта.
     */
    fun toggleAccountThemeMode() {
        when (vaadinService.getSessionThemeMode()!!) {
            ThemeMode.BRIGHT -> setAccountThemeModeDark()
            ThemeMode.DARK -> setAccountThemeModeBright()
        }
    }

    /**
     * Включает светлый режим аккаунта.
     */
    fun setAccountThemeModeBright() {
        val username = vaadinService.getSessionAccountUsername()
        val currentThemeMode = vaadinService.getSessionThemeMode()

        if (currentThemeMode != ThemeMode.BRIGHT && username is String) {
            accountService.setThemeModeByUsername(username, ThemeMode.BRIGHT)
            vaadinService.setSessionThemeMode(ThemeMode.BRIGHT) {
                setImageBright()
                setThemeModeChangeButtonBright()
            }
        }
    }

    /**
     * Включает тёмный режим аккаунта.
     */
    fun setAccountThemeModeDark() {
        val username = vaadinService.getSessionAccountUsername()
        val currentThemeMode = vaadinService.getSessionThemeMode()

        if (currentThemeMode != ThemeMode.DARK && username is String) {
            accountService.setThemeModeByUsername(username, ThemeMode.DARK)
            vaadinService.setSessionThemeMode(ThemeMode.DARK) {
                setImageDark()
                setThemeModeChangeButtonDark()
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

}
