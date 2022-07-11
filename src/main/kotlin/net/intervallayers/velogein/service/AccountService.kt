package net.intervallayers.velogein.service

import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.ThemeMode
import net.intervallayers.velogein.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository, private val sessionService: SessionService) {
    fun getAccountByUsername(username: String): Account? {
        return accountRepository.findByUsernameIgnoreCase(username)
    }

    fun isAccountExistByUsername(username: String): Boolean {
        return accountRepository.existsAccountByUsernameIgnoreCase(username)
    }

    fun getAccount(): Account {
        val username = sessionService.getSessionAccountUsername()
        return accountRepository.findByUsernameIgnoreCase(username)!!
    }

    fun createAccount(account: Account) {
        if (accountRepository.existsAccountByUsernameIgnoreCase(account.username)) {
            throw AccountCreateAlreadyExistExceptions()
        } else {
            accountRepository.save(account.bCryptEncode())
        }
    }

    fun updateAccount(account: Account) {
        if (account.isUsernameChanged && accountRepository.existsAccountByUsernameIgnoreCase(account.username)) {
            throw AccountUpdateAlreadyExistExceptions()
        }

        accountRepository.save(account)
    }

    fun getThemeMode(): ThemeMode {
        val username = sessionService.getSessionAccountUsername()

        return accountRepository.findByUsernameIgnoreCase(username)!!.themeMode
    }

    fun setThemeModeBright() {
        val username = sessionService.getSessionAccountUsername()

        accountRepository.findByUsernameIgnoreCaseAndSetThemeMode(username, ThemeMode.BRIGHT)
    }

    fun setThemeModeDark() {
        val username = sessionService.getSessionAccountUsername()

        accountRepository.findByUsernameIgnoreCaseAndSetThemeMode(username, ThemeMode.DARK)
    }

    class AccountCreateAlreadyExistExceptions : RuntimeException("Пользователь с таким именем уже существует.")
    class AccountUpdateAlreadyExistExceptions : RuntimeException("Пользователь с таким именем уже существует.")
}
