package net.intervallayers.velogein.model

import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class Role {
    USER,
    EDITOR,
    ADMIN;

    fun getAuthority(): SimpleGrantedAuthority {
        return SimpleGrantedAuthority("ROLE_$this")
    }
}
