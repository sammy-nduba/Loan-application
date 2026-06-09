package com.coopbank.loanapp.domain.repository

import com.coopbank.loanapp.domain.model.LoanApplication
import com.coopbank.loanapp.domain.model.LoanCalculation
import kotlinx.coroutines.flow.Flow

interface LoanRepository {
    fun getAllApplications(): Flow<List<LoanApplication>>
    suspend fun applyForLoan(application: LoanApplication)
    
    fun getSavedCalculations(): Flow<List<LoanCalculation>>
    suspend fun saveCalculation(calculation: LoanCalculation)
    suspend fun deleteCalculation(calculation: LoanCalculation)
}
