package net.intervallayers.velogein.utils

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

/**
 * Делегированное установление значения переменной.
 * Позволяет при изменении значения переменной вызывать заготовленную процедуру.
 */
inline fun <T> bind(initialValue: T, crossinline onChange: (newValue: T) -> Unit) = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        onChange(newValue)
    }
}
