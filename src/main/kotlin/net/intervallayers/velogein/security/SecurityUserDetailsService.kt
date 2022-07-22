package net.intervallayers.velogein.security

import net.intervallayers.velogein.service.AccountService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SecurityUserDetailsService(var accountService: AccountService) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val account = accountService.getAccountByUsername(username)

        if (account == null) {
            throw UsernameNotFoundException("Пользователь с именем: $username не найден.")
        } else {
            return User(account.username, account.password, account.roles.map { it.getAuthority() })
        }
    }

}
