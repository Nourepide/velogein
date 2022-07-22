package net.intervallayers.velogein.model

import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.validation.constraints.NotEmpty

/**
 * Резидент, тот кто имеет права на использование депозитов.
 *
 * @author Nourepide@gmail.com
 */
@Entity
data class Domicile(
    @Id
    @GeneratedValue
    var id: UUID? = null,

    @NotEmpty
    var firstName: String,

    @NotEmpty
    var lastName: String,

    @OneToOne(
        mappedBy = "domicile",
        cascade = [CascadeType.MERGE]
    )
    var account: Account? = null,
) {
    companion object {
        fun createEmpty(): Domicile {
            return Domicile(
                firstName = "",
                lastName = ""
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Domicile

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()

        return result
    }
}
