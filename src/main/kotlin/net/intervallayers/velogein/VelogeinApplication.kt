package net.intervallayers.velogein

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Theme(value = "velogein")
class VelogeinApplication : AppShellConfigurator

fun main(args: Array<String>) {
    runApplication<VelogeinApplication>(*args)
}
