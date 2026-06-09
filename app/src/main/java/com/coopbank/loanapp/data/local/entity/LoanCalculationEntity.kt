package com.coopbank.loanapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_calculations")
data class LoanCalculationEntity(
    @PrimaryKey
    val id: String,
    val loanTypeName: String,
    val amount: Double,
    val interestRate: Double,
    val durationMonths: Int,
    val monthlyPayment: Double,
    val totalInterest: Double,
    val totalPayable: Double,
    val date: Long,
    val isSaved: Boolean
)
