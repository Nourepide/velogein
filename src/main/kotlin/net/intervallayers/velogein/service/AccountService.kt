package net.intervallayers.velogein.service

import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(val accountRepository: AccountRepository) {
    fun save(account: Account) {
        if (accountRepository.existsAccountByUsername(account.username)) {
            throw AccountSaveAlreadyExistExceptions()
        } else {
            accountRepository.save(account.bCryptEncode())
        }
    }

    class AccountSaveAlreadyExistExceptions : RuntimeException()
}
