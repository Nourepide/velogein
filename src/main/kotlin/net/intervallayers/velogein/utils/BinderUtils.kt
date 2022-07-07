package net.intervallayers.velogein.utils

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

inline fun <T> bind(initialValue: T, crossinline onChange: (newValue: T) -> Unit) = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        onChange(newValue)
    }
}
