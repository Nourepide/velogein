package net.intervallayers.velogein.service

import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.ThemeMode
import net.intervallayers.velogein.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(val accountRepository: AccountRepository) {
    fun save(account: Account) {
        if (accountRepository.existsAccountByUsernameIgnoreCase(account.username)) {
            throw AccountSaveAlreadyExistExceptions()
        } else {
            accountRepository.save(account.bCryptEncode())
        }
    }

    fun getThemeModeByUsername(username: String): ThemeMode {
        return accountRepository.findByUsernameIgnoreCase(username)!!.themeMode
    }

    fun setThemeModeByUsername(username: String, themeMode: ThemeMode) {
        accountRepository.findByUsernameIgnoreCaseAndSetThemeMode(username, themeMode)
    }

    class AccountSaveAlreadyExistExceptions : RuntimeException()
}
