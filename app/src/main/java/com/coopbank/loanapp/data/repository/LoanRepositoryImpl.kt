package com.coopbank.loanapp.data.repository

import com.coopbank.loanapp.data.local.LoanDao
import com.coopbank.loanapp.data.local.entity.LoanApplicationEntity
import com.coopbank.loanapp.domain.model.LoanApplication
import com.coopbank.loanapp.domain.repository.LoanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoanRepositoryImpl(private val loanDao: LoanDao) : LoanRepository {
    override fun getAllApplications(): Flow<List<LoanApplication>> {
        return loanDao.getAllApplications().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun applyForLoan(application: LoanApplication) {
        loanDao.insertApplication(application.toEntity())
    }

    private fun LoanApplicationEntity.toDomain(): LoanApplication {
        return LoanApplication(
            id = id,
            loanTypeId = loanTypeId,
            amount = amount,
            durationMonths = durationMonths,
            status = status,
            applicationDate = applicationDate
        )
    }

    private fun LoanApplication.toEntity(): LoanApplicationEntity {
        return LoanApplicationEntity(
            id = id,
            loanTypeId = loanTypeId,
            amount = amount,
            durationMonths = durationMonths,
            status = status,
            applicationDate = applicationDate
        )
    }
}
