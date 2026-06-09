package com.coopbank.loanapp.domain.model

import java.util.UUID

data class LoanType(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val longDescription: String = "",
    val backgroundColor: Long,
    val imageResId: Int? = null,
    val interestRate: Double = 0.15,
    val maxAmount: Double = 500000.0,
    val maxDurationMonths: Int = 12,
    val features: List<String> = emptyList()
)

data class LoanApplication(
    val id: String = UUID.randomUUID().toString(),
    val loanTypeId: String,
    val amount: Double,
    val durationMonths: Int,
    val status: LoanStatus = LoanStatus.PENDING,
    val applicationDate: Long = System.currentTimeMillis()
)

data class AmortizationEntry(
    val month: Int,
    val monthlyPayment: Double,
    val principal: Double,
    val interest: Double,
    val remainingBalance: Double
)

data class LoanCalculation(
    val id: String = UUID.randomUUID().toString(),
    val loanTypeName: String,
    val amount: Double,
    val interestRate: Double,
    val durationMonths: Int,
    val monthlyPayment: Double,
    val totalInterest: Double,
    val totalPayable: Double,
    val date: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false
)

enum class LoanStatus {
    PENDING, APPROVED, REJECTED
}
