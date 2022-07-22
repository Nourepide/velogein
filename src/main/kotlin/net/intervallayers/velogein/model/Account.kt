package net.intervallayers.velogein.model

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.validation.constraints.NotBlank

/**
 * Модель пользователя для обработки аунтефикации и
 * сохранения данных применимых к пользователю, а не к резиденту.
 *
 * @author Nourepide@gmail.com
 */
@Entity
data class Account(
    @Id
    @GeneratedValue
    var id: UUID? = null,

    @NotBlank
    @Column(unique = true)
    var username: String,

    @NotBlank
    var password: String,

    var firstName: String,

    var lastName: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "domicile_id", unique = true)
    var domicile: Domicile? = null,

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableSet<Role>,

    @Enumerated(EnumType.STRING)
    var themeMode: ThemeMode,
) {
    companion object {
        /**
         * Создаёт пустого пользователя.
         * Все необязательные поля не заполняются, обязательные поля пустые.
         */
        fun createEmpty(): Account {
            return Account(
                username = "",
                password = "",
                firstName = "",
                lastName = "",
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

    @Transient
    var passwordDecode: String? = null

    @Transient
    var isUsernameChanged: Boolean = false
}
