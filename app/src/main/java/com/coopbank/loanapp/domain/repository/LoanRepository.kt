package com.coopbank.loanapp.domain.repository

import com.coopbank.loanapp.domain.model.LoanApplication
import kotlinx.coroutines.flow.Flow

interface LoanRepository {
    fun getAllApplications(): Flow<List<LoanApplication>>
    suspend fun applyForLoan(application: LoanApplication)
}
