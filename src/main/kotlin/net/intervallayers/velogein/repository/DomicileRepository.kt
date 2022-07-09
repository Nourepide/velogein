package net.intervallayers.velogein.repository

import net.intervallayers.velogein.model.Domicile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DomicileRepository : JpaRepository<Domicile, UUID>
