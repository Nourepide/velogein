package net.intervallayers.velogein.security

import net.intervallayers.velogein.repository.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SecurityUserDetailsService(var accountRepository: AccountRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val account = accountRepository.findByUsernameIgnoreCase(username)

        if (account == null) {
            throw UsernameNotFoundException("Пользователь с именем: $username не найден.")
        } else {
            return User(account.username, account.password, account.authorities)
        }
    }

}
