package net.intervallayers.velogein

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dependency.NpmPackage
import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
@CssImport("styles.css")
@NpmPackage(value = "line-awesome", version = "1.3.0")
class VelogeinApplication

fun main(args: Array<String>) {
    runApplication<VelogeinApplication>(*args)
}
