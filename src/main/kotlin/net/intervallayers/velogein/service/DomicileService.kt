package net.intervallayers.velogein.service

import net.intervallayers.velogein.model.Domicile
import net.intervallayers.velogein.repository.DomicileRepository
import org.springframework.stereotype.Service

@Service
class DomicileService(private val domicileRepository: DomicileRepository) {

    fun findAll(): MutableList<Domicile> {
        return domicileRepository.findAll()
    }

    fun findAllWithoutAccountExcept(username: String): MutableSet<Domicile> {
        return domicileRepository.findAllByAccount_UsernameOrAccount_IdIsNull(username)
    }

    fun domicileIsExist(firstName: String, lastName: String): Boolean {
        return domicileRepository.existsDomicileByFirstNameAndLastName(firstName, lastName)
    }

    fun domicileIsExist(domicile: Domicile): Boolean {
        return domicileIsExist(domicile.firstName, domicile.lastName)
    }

    fun createDomicile(domicile: Domicile) {
        if (domicileIsExist(domicile)) {
            throw DomicileAlreadyExistException()
        } else {
            domicileRepository.save(domicile)
        }
    }

    fun deleteDomicile(domicile: Domicile) {
        if (domicile.account != null) {
            domicile.account!!.domicile = null
        }

        domicileRepository.delete(domicile)
    }

    class DomicileAlreadyExistException : RuntimeException("Резидент с таким именем уже существует.")

}
