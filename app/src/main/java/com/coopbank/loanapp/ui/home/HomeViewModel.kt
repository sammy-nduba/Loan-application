package com.coopbank.loanapp.ui.home

import androidx.lifecycle.ViewModel
import com.coopbank.loanapp.R
import com.coopbank.loanapp.domain.model.LoanType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.coopbank.loanapp.domain.repository.LoanRepository
import com.coopbank.loanapp.domain.model.LoanApplication
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val repository: LoanRepository) : ViewModel() {
    private val _loanTypes = MutableStateFlow<List<LoanType>>(emptyList())
    val loanTypes: StateFlow<List<LoanType>> = _loanTypes

    val applications: StateFlow<List<LoanApplication>> = repository.getAllApplications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        _loanTypes.value = listOf(
            LoanType(
                name = "Salary E-Loan",
                description = "Get quick loans to boost your income",
                longDescription = "A convenient loan for salaried individuals to meet their urgent financial needs with minimal documentation.",
                backgroundColor = 0xFF1B5E20,
                imageResId = R.drawable.loans_1,
                interestRate = 0.12,
                maxAmount = 500000.0,
                maxDurationMonths = 24,
                features = listOf("Instant approval", "Low interest rates", "Flexible repayment", "Salary-linked installments")
            ),
            LoanType(
                name = "Buy Now Pay Later",
                description = "Buy goods today, pay later",
                longDescription = "Finance your shopping with our BNPL option and pay in easy installments at over 500 partner outlets.",
                backgroundColor = 0xFF01579B,
                imageResId = R.drawable.bnpl,
                interestRate = 0.15,
                maxAmount = 100000.0,
                maxDurationMonths = 6,
                features = listOf("Partnered stores", "No hidden charges", "Easy setup", "Pay within 3-6 months")
            ),
            LoanType(
                name = "Stock Loan",
                description = "Boost your business stock today",
                longDescription = "Grow your business by restocking with our quick business financing solutions designed for SMEs.",
                backgroundColor = 0xFF8D4F00,
                imageResId = R.drawable.stock_finance,
                interestRate = 0.14,
                maxAmount = 2000000.0,
                maxDurationMonths = 36,
                features = listOf("Higher limits", "Grace periods", "Business support", "Competitive rates")
            )
        )
    }
}
