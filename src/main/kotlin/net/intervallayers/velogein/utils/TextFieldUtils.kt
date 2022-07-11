package net.intervallayers.velogein.utils

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField

/**
 * Устанавливает текстовое поле обязательным для заполнения.
 * Параметр minimumLength - определяет минимальное количество символов для поля.
 */
fun TextField.setRequiredNotEmpty(minimumLength: Int = 1) {
    addFocusListener {
        isInvalid = false
    }
    addBlurListener {
        isInvalid = value.trim().length < minimumLength
    }
    isRequired = true
}

/**
 * Позволяет добавить событие при нажатии на клавишу Enter.
 */
fun TextField.addEnterKeyListener(block: () -> Unit) {
    addKeyPressListener {
        if (it.key == Key.ENTER) {
            block.invoke()
        }
    }
}

/**
 * Устанавливает текстовое поле обязательным для заполнения.
 * Параметр minimumLength - определяет минимальное количество символов для поля.
 */
fun PasswordField.setRequiredNotEmpty(minimumLength: Int = 1) {
    addFocusListener {
        isInvalid = false
    }
    addBlurListener {
        isInvalid = value.trim().length < minimumLength
    }
    isRequired = true
}

/**
 * Устанавливает текстовое поле обязательным для заполнения и
 * обязательным совпадением с другим текстовым полем.
 * Параметр minimumLength - определяет минимальное количество символов для поля.
 */
fun PasswordField.setRequiredNotEmptyAndEquals(passwordField: PasswordField, minimumLength: Int = 1) {
    addFocusListener {
        isInvalid = false
    }
    addBlurListener {
        val lengthNotZero = value.trim().length < minimumLength
        val passwordNotMatch = value != passwordField.value

        isInvalid = lengthNotZero || passwordNotMatch
    }
    addValueChangeListener {
        if (isEmpty) {
            isInvalid = false
        }
    }
    passwordField.addValueChangeListener {
        if (!(value.trim().length < minimumLength && !isInvalid)) {
            isInvalid = value != passwordField.value
        }
    }
    isRequired = true
}

/**
 * Устанавливает текстовое поле обязательным для совпадения с другим текстовым полем.
 */
fun PasswordField.setRequiredEquals(passwordField: PasswordField) {
    addFocusListener {
        isInvalid = false
    }
    addBlurListener {
        val passwordNotMatch = value != passwordField.value

        isInvalid = passwordNotMatch
    }
    addValueChangeListener {
        if (isEmpty) {
            isInvalid = false
        }
    }
    passwordField.addValueChangeListener {
        if (value.isNotEmpty()) {
            isInvalid = value != passwordField.value
        }

        if (value.isEmpty() && passwordField.value.isEmpty()) {
            isInvalid = false
        }
    }
}

/**
 * Позволяет добавить событие при нажатии на клавишу Enter.
 */
fun PasswordField.addEnterKeyListener(block: () -> Unit) {
    addKeyPressListener {
        if (it.key == Key.ENTER) {
            block.invoke()
        }
    }
}
