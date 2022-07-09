package net.intervallayers.velogein.repository

import net.intervallayers.velogein.model.Deposit
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DepositRepository : JpaRepository<Deposit, UUID>
