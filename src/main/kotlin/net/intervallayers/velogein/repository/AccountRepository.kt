package net.intervallayers.velogein.repository

import net.intervallayers.velogein.model.Account
import net.intervallayers.velogein.model.ThemeMode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByUsernameIgnoreCase(username: String): Account?
    fun existsAccountByUsernameIgnoreCase(username: String): Boolean

    @Modifying
    @Transactional
    @Query("update Account a set a.themeMode = :themeMode where a.username = :username")
    fun findByUsernameIgnoreCaseAndSetThemeMode(@Param("username") username: String, @Param("themeMode") themeMode: ThemeMode)
}
