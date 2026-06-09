package com.coopbank.loanapp.ui.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coopbank.loanapp.domain.model.LoanApplication
import com.coopbank.loanapp.domain.model.LoanStatus
import com.coopbank.loanapp.domain.repository.LoanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class RepaymentInstallment(
    val label: String,
    val amount: String
)

data class ApplyLoanUiState(
    val selectedLoanType: String = "Salary E-Loan",
    val amount: String = "10,000",
    val periodMonths: Int = 2,
    val disbursementAccount: String = "011090145246100",
    val interestRate: Double = 0.15,
    val loanLimit: Double = 12000.0,
    val loanTypes: List<String> = listOf("Salary E-Loan", "Buy Now Pay Later", "Stock Loan"),
    val accounts: List<String> = listOf("011090145246100", "011090145246200"),
    val isSubmitted: Boolean = false,
    val totalPayable: Double = 0.0,
    val installments: List<RepaymentInstallment> = emptyList(),
    val hasActiveLoan: Boolean = false
)

class ApplyLoanViewModel(private val repository: LoanRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ApplyLoanUiState())
    val uiState: StateFlow<ApplyLoanUiState> = _uiState.asStateFlow()

    init {
        checkActiveLoans()
        calculateRepayment()
    }

    private fun checkActiveLoans() {
        viewModelScope.launch {
            repository.getAllApplications().collect { applications ->
                val hasActive = applications.any { it.status == LoanStatus.APPROVED || it.status == LoanStatus.PENDING }
                _uiState.update { it.copy(hasActiveLoan = hasActive) }
            }
        }
    }

    fun setInitialLoanType(loanType: String?) {
        if (loanType != null && loanType in _uiState.value.loanTypes) {
            _uiState.update { it.copy(selectedLoanType = loanType) }
            calculateRepayment()
        }
    }

    fun onAmountChange(newAmount: String) {
        _uiState.update { it.copy(amount = newAmount) }
        calculateRepayment()
    }

    fun onLoanTypeSelected(loanType: String) {
        _uiState.update { it.copy(selectedLoanType = loanType) }
        calculateRepayment()
    }

    fun onPeriodSelected(months: Int) {
        _uiState.update { it.copy(periodMonths = months) }
        calculateRepayment()
    }

    private fun calculateRepayment() {
        val amountValue = _uiState.value.amount.replace(",", "").toDoubleOrNull() ?: 0.0
        val interest = amountValue * _uiState.value.interestRate
        val total = amountValue + interest
        val monthlyPayment = if (_uiState.value.periodMonths > 0) total / _uiState.value.periodMonths else total
        
        val installments = mutableListOf<RepaymentInstallment>()
        val calendar = java.util.Calendar.getInstance()
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        
        for (i in 1.._uiState.value.periodMonths) {
            calendar.add(java.util.Calendar.MONTH, 1)
            val suffix = when (i) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
            installments.add(
                RepaymentInstallment(
                    label = "$i$suffix instalment - ${dateFormat.format(calendar.time)}",
                    amount = "%,.2f KES".format(monthlyPayment)
                )
            )
        }

        _uiState.update { it.copy(
            totalPayable = total,
            installments = installments
        ) }
    }

    fun onAccountSelected(account: String) {
        _uiState.update { it.copy(disbursementAccount = account) }
    }

    fun submitApplication() {
        val currentState = _uiState.value
        val amountValue = currentState.amount.replace(",", "").toDoubleOrNull() ?: 0.0
        
        val application = LoanApplication(
            id = UUID.randomUUID().toString(),
            loanTypeId = currentState.selectedLoanType,
            amount = amountValue,
            durationMonths = currentState.periodMonths,
            status = LoanStatus.PENDING,
            applicationDate = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.applyForLoan(application)
            _uiState.update { it.copy(isSubmitted = true) }
        }
    }
}
