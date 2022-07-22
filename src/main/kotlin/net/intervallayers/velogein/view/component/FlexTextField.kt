package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.theme.lumo.LumoUtility

/**
 * Обёртка над текстовым полем.
 * Устанавливает максимальную ширину поля.
 *
 * @author Nourepide@gmail.com
 */
class FlexTextField(label: String?) : TextField(label) {
    init {
        addClassName(LumoUtility.Width.FULL)
    }
}
