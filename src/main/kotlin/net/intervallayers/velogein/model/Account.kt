package net.intervallayers.velogein.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

/**
 * Модель пользователя для обработки аунтефикации и
 * сохранения данных применимых к пользователю, а не к резиденту.
 */
@Entity
data class Account(
    @Id
    @GeneratedValue
    var id: UUID?,

    @Column(unique = true)
    var username: String,
    var password: String,
    var isActive: Boolean,
    var firstName: String,
    var lastName: String,

    @ManyToOne
    var domicile: Domicile?,

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableSet<Role>,

    @Enumerated(EnumType.STRING)
    var themeMode: ThemeMode,
) {
    companion object {
        fun createEmpty(): Account {
            return Account(
                id = null,
                username = "",
                password = "",
                isActive = true,
                firstName = "",
                lastName = "",
                domicile = null,
                roles = mutableSetOf(Role.USER),
                themeMode = ThemeMode.BRIGHT
            )
        }
    }

    fun bCryptEncode(): Account {
        if (passwordDecode != null && passwordDecode!!.isNotEmpty()) {
            BCryptPasswordEncoder(12)
                .encode(passwordDecode)
                .also { password = it }
        }

        return this
    }

    @get:JsonIgnore
    val authorities: Collection<GrantedAuthority>
        get() = roles
            .map { SimpleGrantedAuthority("ROLE_$it") }
            .toList()

    @Transient
    var passwordDecode: String? = null

    @Transient
    var isUsernameChanged: Boolean = false
}
