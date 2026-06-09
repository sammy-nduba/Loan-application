package com.coopbank.loanapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coopbank.loanapp.domain.model.LoanStatus

@Entity(tableName = "loan_applications")
data class LoanApplicationEntity(
    @PrimaryKey
    val id: String,
    val loanTypeId: String,
    val amount: Double,
    val durationMonths: Int,
    val status: LoanStatus,
    val applicationDate: Long
)
