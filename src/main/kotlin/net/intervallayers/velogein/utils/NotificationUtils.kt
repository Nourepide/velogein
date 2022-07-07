package net.intervallayers.velogein.utils

import com.vaadin.flow.component.Text
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import java.time.Duration

/**
 * Обёртка для создания успешных уведомлений.
 */
fun createSuccessNotification(text: String) {
    createNotification(NotificationVariant.LUMO_SUCCESS, VaadinIcon.CHECK_CIRCLE.create(), text, Duration.ofSeconds(2))
}

/**
 * Обёртка для создания ошибочных уведомлений.
 */
fun createErrorNotification(text: String) {
    createNotification(NotificationVariant.LUMO_ERROR, VaadinIcon.CLOSE_CIRCLE.create(), text, Duration.ofSeconds(2))
}

/**
 * Обёртка для быстрого создания уведомлений.
 */
fun createNotification(variant: NotificationVariant, icon: Icon, text: String, duration: Duration) {
    val horizontalLayout = HorizontalLayout(icon, Div(Text(text)).apply { width = "100%" })
        .apply { alignItems = FlexComponent.Alignment.CENTER }

    with(Notification()) {
        this.duration = duration.toMillis().toInt()

        add(horizontalLayout)
        addThemeVariants(variant)
        open()
    }
}
