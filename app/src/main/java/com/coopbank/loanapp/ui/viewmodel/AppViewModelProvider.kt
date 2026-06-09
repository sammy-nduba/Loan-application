package com.coopbank.loanapp.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coopbank.loanapp.LoanApp
import com.coopbank.loanapp.ui.apply.ApplyLoanViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ApplyLoanViewModel(loanApp().repository)
        }
    }
}

fun CreationExtras.loanApp(): LoanApp =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LoanApp)
