package com.coopbank.loanapp.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.coopbank.loanapp.domain.model.AmortizationEntry
import com.coopbank.loanapp.domain.model.LoanCalculation
import java.text.DecimalFormat

import com.coopbank.loanapp.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanCalculatorScreen(
    onBack: () -> Unit,
    viewModel: LoanCalculatorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Calculate", "Schedule", "History")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Loan Calculator", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF004D40)
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF004D40),
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF8BC34A)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> CalculatorTab(uiState, viewModel)
                1 -> ScheduleTab(uiState.amortizationSchedule)
                2 -> HistoryTab(uiState.history)
            }
        }
    }
}

@Composable
fun CalculatorTab(uiState: CalculatorUiState, viewModel: LoanCalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.amount,
            onValueChange = { viewModel.onAmountChange(it) },
            label = { Text("Loan Amount (KES)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = uiState.interestRate,
            onValueChange = { viewModel.onInterestRateChange(it) },
            label = { Text("Annual Interest Rate (%)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        OutlinedTextField(
            value = uiState.durationMonths,
            onValueChange = { viewModel.onDurationChange(it) },
            label = { Text("Duration (Months)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = { viewModel.calculateLoan() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A))
        ) {
            Text("Calculate EMI")
        }

        uiState.calculationResult?.let { result ->
            CalculationResultCard(result)
        }
    }
}

@Composable
fun CalculationResultCard(result: LoanCalculation) {
    val df = DecimalFormat("#,##0.00")
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Monthly EMI", fontWeight = FontWeight.Bold, color = Color.Gray)
            Text("KES ${df.format(result.monthlyPayment)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            ResultRow("Total Interest", "KES ${df.format(result.totalInterest)}")
            ResultRow("Total Amount", "KES ${df.format(result.totalPayable)}")
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ScheduleTab(schedule: List<AmortizationEntry>) {
    if (schedule.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Calculate a loan to see schedule", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFEEEEEE)).padding(8.dp)) {
                    Text("Month", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("Principal", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                    Text("Interest", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                    Text("Balance", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                }
            }
            items(schedule) { entry ->
                AmortizationRow(entry)
            }
        }
    }
}

@Composable
fun AmortizationRow(entry: AmortizationEntry) {
    val df = DecimalFormat("#,##0")
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("${entry.month}", modifier = Modifier.weight(1f))
        Text(df.format(entry.principal), modifier = Modifier.weight(2f))
        Text(df.format(entry.interest), modifier = Modifier.weight(2f))
        Text(df.format(entry.remainingBalance), modifier = Modifier.weight(2f))
    }
}

@Composable
fun HistoryTab(history: List<LoanCalculation>) {
    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No past calculations", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(history) { calc ->
                CalculationHistoryItem(calc)
            }
        }
    }
}

@Composable
fun CalculationHistoryItem(calc: LoanCalculation) {
    val df = DecimalFormat("#,##0")
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Amount: KES ${df.format(calc.amount)}", fontWeight = FontWeight.Bold)
                Text("${calc.durationMonths} Mos", color = Color.Gray)
            }
            Text("EMI: KES ${df.format(calc.monthlyPayment)}", color = Color(0xFF2E7D32))
        }
    }
}
