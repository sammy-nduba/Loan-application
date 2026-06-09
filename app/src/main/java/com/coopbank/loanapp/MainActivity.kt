package com.coopbank.loanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coopbank.loanapp.ui.apply.ApplyLoanScreen
import com.coopbank.loanapp.ui.apply.ConfirmLoanScreen
import com.coopbank.loanapp.ui.home.HomeScreen
import com.coopbank.loanapp.ui.home.HomeViewModel
import com.coopbank.loanapp.ui.home.LoanDetailsScreen
import com.coopbank.loanapp.ui.success.SuccessScreen
import com.coopbank.loanapp.ui.calculator.LoanCalculatorScreen
import com.coopbank.loanapp.ui.theme.CoopBankTheme
import com.coopbank.loanapp.ui.apply.ApplyLoanViewModel
import com.coopbank.loanapp.ui.viewmodel.AppViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoopBankTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoanAppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoanAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val applyLoanViewModel: ApplyLoanViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val loanTypes by homeViewModel.loanTypes.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                onApplyClick = { loan -> 
                    navController.navigate("details/${loan.id}") 
                },
                onCalculatorClick = {
                    navController.navigate("calculator")
                },
                viewModel = homeViewModel
            )
        }
        composable("details/{loanId}") { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId")
            val loan = loanTypes.find { it.id == loanId }
            loan?.let {
                LoanDetailsScreen(
                    loan = it,
                    onBack = { navController.popBackStack() },
                    onApply = { navController.navigate("apply") }
                )
            }
        }
        composable("calculator") {
            LoanCalculatorScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("apply") {
            ApplyLoanScreen(
                onNext = { navController.navigate("confirm") },
                onBack = { navController.popBackStack() },
                viewModel = applyLoanViewModel
            )
        }
        composable("confirm") {
            ConfirmLoanScreen(
                onConfirm = { 
                    applyLoanViewModel.submitApplication()
                    navController.navigate("success") 
                },
                onBack = { navController.popBackStack() },
                viewModel = applyLoanViewModel
            )
        }
        composable("success") {
            SuccessScreen(onDone = {
                navController.popBackStack("home", inclusive = false)
            })
        }
    }
}
