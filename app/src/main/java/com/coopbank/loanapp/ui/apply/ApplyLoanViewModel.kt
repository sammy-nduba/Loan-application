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

data class ApplyLoanUiState(
    val selectedLoanType: String = "Salary E-Loan",
    val amount: String = "10,000.00",
    val periodMonths: Int = 2,
    val disbursementAccount: String = "011090145246100",
    val interestRate: Double = 0.15,
    val loanLimit: Double = 12000.0,
    val loanTypes: List<String> = listOf("Salary E-Loan", "Buy Now Pay Later", "Stock Loan"),
    val accounts: List<String> = listOf("011090145246100", "011090145246200"),
    val isSubmitted: Boolean = false
)

class ApplyLoanViewModel(private val repository: LoanRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ApplyLoanUiState())
    val uiState: StateFlow<ApplyLoanUiState> = _uiState.asStateFlow()

    fun onAmountChange(newAmount: String) {
        _uiState.update { it.copy(amount = newAmount) }
    }

    fun onLoanTypeSelected(loanType: String) {
        _uiState.update { it.copy(selectedLoanType = loanType) }
    }

    fun onPeriodSelected(months: Int) {
        _uiState.update { it.copy(periodMonths = months) }
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
