package com.coopbank.loanapp

import android.app.Application
import com.coopbank.loanapp.data.local.AppDatabase
import com.coopbank.loanapp.data.repository.LoanRepositoryImpl
import com.coopbank.loanapp.domain.repository.LoanRepository

class LoanApp : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository: LoanRepository by lazy { LoanRepositoryImpl(database.loanDao()) }
}
