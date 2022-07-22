package net.intervallayers.velogein.view.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button

/**
 * Кнопка со состоянием.
 * При нажатии переключает своё состояние на противоположное.
 * Позволяет привязать разные изображения на состояние кнопки.
 *
 * @author Nourepide@gmail.com
 */
class StateButton : Button() {

    var isPressed = false

    private var imageIfPressed: Component? = null
    private var imageIfUnpressed: Component? = null

    init {
        addClickListener {
            toggleIsPressed()
        }
    }

    /**
     * Переключает состояние кнопки на альтернативное.
     * Так-же изменяет иконку если указана.
     */
    fun toggleIsPressed() {
        isPressed = !isPressed

        if (isPressed) {
            this.icon = imageIfPressed
        }

        if (!isPressed) {
            this.icon = imageIfUnpressed
        }
    }

    /**
     * Устанавливает иконку для нажатой кнопки.
     */
    fun setImageIfPressed(icon: Component?) {
        imageIfPressed = icon

        if (isPressed) {
            this.icon = imageIfPressed
        }
    }

    /**
     * Устанавливает иконку для не нажатой кнопки.
     */
    fun setImageIfUnpressed(icon: Component?) {
        imageIfUnpressed = icon

        if (!isPressed) {
            this.icon = imageIfUnpressed
        }
    }
}
