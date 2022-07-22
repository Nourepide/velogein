package net.intervallayers.velogein.repository

import net.intervallayers.velogein.model.Domicile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@Suppress("FunctionName")
interface DomicileRepository : JpaRepository<Domicile, UUID> {

    fun findAllByAccount_UsernameOrAccount_IdIsNull(username: String): MutableSet<Domicile>

    fun existsDomicileByFirstNameAndLastName(firstName: String, lastName: String): Boolean

}
