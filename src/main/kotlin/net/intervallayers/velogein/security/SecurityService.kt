package net.intervallayers.velogein.security

import com.vaadin.flow.server.VaadinServletRequest
import org.springframework.stereotype.Service

@Service
class SecurityService {

    fun login(username: String, password: String) {
        VaadinServletRequest
            .getCurrent()
            .login(username, password)
    }

    fun logout() {
        VaadinServletRequest
            .getCurrent()
            .logout()
    }

}
