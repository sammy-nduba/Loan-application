package com.coopbank.loanapp.data.local

import androidx.room.*
import com.coopbank.loanapp.data.local.entity.LoanApplicationEntity
import com.coopbank.loanapp.data.local.entity.LoanCalculationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Query("SELECT * FROM loan_applications ORDER BY applicationDate DESC")
    fun getAllApplications(): Flow<List<LoanApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: LoanApplicationEntity)

    @Delete
    suspend fun deleteApplication(application: LoanApplicationEntity)

    @Query("SELECT * FROM loan_calculations WHERE isSaved = 1 ORDER BY date DESC")
    fun getSavedCalculations(): Flow<List<LoanCalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: LoanCalculationEntity)

    @Delete
    suspend fun deleteCalculation(calculation: LoanCalculationEntity)
}
