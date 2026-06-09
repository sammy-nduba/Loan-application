package com.coopbank.loanapp.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coopbank.loanapp.LoanApp
import com.coopbank.loanapp.ui.apply.ApplyLoanViewModel
import com.coopbank.loanapp.ui.home.HomeViewModel
import com.coopbank.loanapp.ui.calculator.LoanCalculatorViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ApplyLoanViewModel(loanApp().repository)
        }
        initializer {
            HomeViewModel(loanApp().repository)
        }
        initializer {
            LoanCalculatorViewModel(loanApp().repository)
        }
    }
}

fun CreationExtras.loanApp(): LoanApp =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LoanApp)
