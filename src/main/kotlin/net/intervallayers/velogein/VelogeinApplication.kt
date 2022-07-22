package net.intervallayers.velogein

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@PWA(
    name = "Византийская литания | Казначейство",
    shortName = "Казначейство",
    offlinePath = "offline.html",
    offlineResources = ["./images/logo_bright.svg"]
)
@SpringBootApplication
@Theme(value = "velogein")
class VelogeinApplication : AppShellConfigurator

fun main(args: Array<String>) {
    runApplication<VelogeinApplication>(*args)
}
