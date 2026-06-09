package com.coopbank.loanapp.ui.apply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.coopbank.loanapp.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLoanScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: ApplyLoanViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.hasActiveLoan) {
        AlertDialog(
            onDismissRequest = onBack,
            confirmButton = {
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7AC143))) {
                    Text("OK")
                }
            },
            title = { Text("Active Loan Found", fontWeight = FontWeight.Bold) },
            text = { Text("You cannot apply for a new loan while still having an unpaid loan. Please clear your current loan first.") },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            ApplyLoanHeader(onBack = onBack)
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Apply Loan", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Loan Type Dropdown
            LabelText("Loan Type")
            LoanDropdown(
                selectedOption = uiState.selectedLoanType,
                options = uiState.loanTypes,
                onOptionSelected = { viewModel.onLoanTypeSelected(it) }
            )
            
            Text(
                text = "Interest: 15% p.a",
                color = Color.Gray,
                fontSize = 14.sp
            )

            // Loan Amount
            LabelText("Loan Amount")
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Row(
                        modifier = Modifier.padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("KES", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Divider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.Gray)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(8.dp)
            )
            Text(
                text = "Available Loan Limit: 12,000.00 KES",
                color = Color(0xFF4CAF50),
                fontSize = 12.sp
            )

            // Loan Period Dropdown
            LabelText("Loan Period (months)")
            LoanDropdown(
                selectedOption = uiState.periodMonths.toString(),
                options = listOf("1", "2", "3", "6", "12"),
                onOptionSelected = { viewModel.onPeriodSelected(it.toInt()) }
            )
            Text(
                text = "Total Amount Payable: %,.2f KES".format(uiState.totalPayable),
                color = Color(0xFF4CAF50),
                fontSize = 12.sp
            )

            // Disbursement Account Dropdown
            LabelText("Disbursement Account")
            LoanDropdown(
                selectedOption = uiState.disbursementAccount,
                options = uiState.accounts,
                onOptionSelected = { viewModel.onAccountSelected(it) }
            )
            Text(
                text = "Available Loan Limit: %,.2f KES".format(uiState.loanLimit),
                color = Color(0xFF4CAF50),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Repayment Schedule
            Text(
                text = "Repayment Schedule",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            uiState.installments.forEach { installment ->
                RepaymentRow(installment.label, installment.amount)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LabelText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDropdown(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ApplyLoanHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF003322), Color(0xFF004433))
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Apply Loan",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }
    }
}

@Composable
fun RepaymentRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
