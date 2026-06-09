package com.coopbank.loanapp.data.local

import androidx.room.TypeConverter
import com.coopbank.loanapp.domain.model.LoanStatus

class Converters {
    @TypeConverter
    fun fromLoanStatus(status: LoanStatus): String {
        return status.name
    }

    @TypeConverter
    fun toLoanStatus(status: String): LoanStatus {
        return LoanStatus.valueOf(status)
    }
}
