package net.intervallayers.velogein.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

/**
 * Депозит для хранения данных валюты и резидентов.
 *
 * @author Nourepide@gmail.com
 */
@Entity
data class Deposit(
    @Id
    @GeneratedValue
    var id: UUID?,

    var name: String?,

    @OneToMany
    val domicile: MutableSet<Domicile>,
)
