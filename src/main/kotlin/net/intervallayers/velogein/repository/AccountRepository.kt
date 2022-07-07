package net.intervallayers.velogein.repository

import net.intervallayers.velogein.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByUsernameIgnoreCase(username: String): Account?
    fun existsAccountByUsername(username: String) : Boolean
}
