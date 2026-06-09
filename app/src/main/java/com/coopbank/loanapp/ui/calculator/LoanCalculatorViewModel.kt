package com.coopbank.loanapp.ui.calculator

import androidx.lifecycle.ViewModel
import com.coopbank.loanapp.domain.model.AmortizationEntry
import com.coopbank.loanapp.domain.model.LoanCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow

data class CalculatorUiState(
    val amount: String = "10000",
    val interestRate: String = "15",
    val durationMonths: String = "12",
    val calculationResult: LoanCalculation? = null,
    val amortizationSchedule: List<AmortizationEntry> = emptyList(),
    val history: List<LoanCalculation> = emptyList(),
    val savedCalculations: List<LoanCalculation> = emptyList()
)

class LoanCalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onAmountChange(value: String) {
        _uiState.update { it.copy(amount = value) }
    }

    fun onInterestRateChange(value: String) {
        _uiState.update { it.copy(interestRate = value) }
    }

    fun onDurationChange(value: String) {
        _uiState.update { it.copy(durationMonths = value) }
    }

    fun calculateLoan() {
        val p = _uiState.value.amount.toDoubleOrNull() ?: return
        val annualRate = _uiState.value.interestRate.toDoubleOrNull() ?: return
        val n = _uiState.value.durationMonths.toIntOrNull() ?: return

        val r = annualRate / 12 / 100
        val emi = if (r == 0.0) p / n else (p * r * (1 + r).pow(n)) / ((1 + r).pow(n) - 1)
        
        val totalPayable = emi * n
        val totalInterest = totalPayable - p

        val result = LoanCalculation(
            loanTypeName = "Custom Calculation",
            amount = p,
            interestRate = annualRate,
            durationMonths = n,
            monthlyPayment = emi,
            totalInterest = totalInterest,
            totalPayable = totalPayable
        )

        val schedule = generateAmortizationSchedule(p, annualRate, n, emi)

        _uiState.update { it.copy(
            calculationResult = result,
            amortizationSchedule = schedule,
            history = (listOf(result) + it.history).take(10)
        ) }
    }

    fun saveCurrentCalculation() {
        _uiState.value.calculationResult?.let { current ->
            val savedCalc = current.copy(isSaved = true)
            _uiState.update { it.copy(
                savedCalculations = (listOf(savedCalc) + it.savedCalculations).distinctBy { c -> 
                    "${c.amount}-${c.interestRate}-${c.durationMonths}" 
                }
            ) }
        }
    }

    fun deleteSavedCalculation(calculation: LoanCalculation) {
        _uiState.update { it.copy(
            savedCalculations = it.savedCalculations.filter { c -> c.id != calculation.id }
        ) }
    }

    private fun generateAmortizationSchedule(
        principal: Double,
        annualRate: Double,
        months: Int,
        emi: Double
    ): List<AmortizationEntry> {
        val schedule = mutableListOf<AmortizationEntry>()
        var remainingBalance = principal
        val monthlyRate = annualRate / 12 / 100

        for (i in 1..months) {
            val interest = remainingBalance * monthlyRate
            val principalPaid = emi - interest
            remainingBalance -= principalPaid
            
            schedule.add(
                AmortizationEntry(
                    month = i,
                    monthlyPayment = emi,
                    principal = principalPaid,
                    interest = interest,
                    remainingBalance = if (remainingBalance < 0) 0.0 else remainingBalance
                )
            )
        }
        return schedule
    }
}
