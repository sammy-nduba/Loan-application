package com.coopbank.loanapp.ui.apply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.coopbank.loanapp.ui.viewmodel.AppViewModelProvider

@Composable
fun ConfirmLoanScreen(
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    viewModel: ApplyLoanViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ApplyLoanHeader(onBack = onBack)
        },
        bottomBar = {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Loan Details Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Loan Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                DetailRow("Loan Amount:", "${uiState.amount} KES", isAmount = true)
                DetailRow("Interest:", "1,500.00 KES")
                DetailRow("Total Charges:", "11,500.00 KES")
                DetailRow("Period:", "${uiState.periodMonths} Months")
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Disbursement Details Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Disbursement Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                DetailRow("Account:", uiState.disbursementAccount)
                DetailRow("Amount:", "${uiState.amount} KES", isBold = true)
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Repayment Details Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Repayment Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                DetailRow("Amount:", "11,500.00 KES", isBold = true)
                DetailRow("Installments:", uiState.periodMonths.toString())
                DetailRow("Next Repayment Date:", "22 Oct 2025")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isAmount: Boolean = false, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(
            text = value,
            fontWeight = if (isAmount || isBold) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isAmount) 18.sp else 14.sp,
            color = if (isAmount) Color(0xFF004D40) else Color.Black
        )
    }
}
