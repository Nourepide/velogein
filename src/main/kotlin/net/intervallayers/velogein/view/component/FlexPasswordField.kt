package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.theme.lumo.LumoUtility

/**
 * Обёртка над полем пароля.
 * Устанавливает максимальную ширину поля.
 *
 * @author Nourepide@gmail.com
 */
class FlexPasswordField(label: String?) : PasswordField(label) {
    init {
        addClassName(LumoUtility.Width.FULL)
    }
}
