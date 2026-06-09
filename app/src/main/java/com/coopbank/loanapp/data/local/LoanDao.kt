package com.coopbank.loanapp.data.local

import androidx.room.*
import com.coopbank.loanapp.data.local.entity.LoanApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Query("SELECT * FROM loan_applications ORDER BY applicationDate DESC")
    fun getAllApplications(): Flow<List<LoanApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: LoanApplicationEntity)

    @Delete
    suspend fun deleteApplication(application: LoanApplicationEntity)
}
