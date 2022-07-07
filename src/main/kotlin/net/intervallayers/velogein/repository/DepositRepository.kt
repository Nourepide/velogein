package net.intervallayers.velogein.repository

import net.intervallayers.velogein.model.*
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DepositRepository: JpaRepository<Deposit, UUID>
