package net.intervallayers.velogein.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Резидент, тот кто имеет права на использование и вклады.
 */
@Entity
data class Domicile(
    @Id
    @GeneratedValue
    var id: UUID?,

    var name: String,
)
