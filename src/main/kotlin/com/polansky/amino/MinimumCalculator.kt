package com.polansky.amino

import com.polansky.amino.food.Food
import com.polansky.amino.service.TotalsCalculator
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class MinimumCalculator(val totalCalculator: TotalsCalculator) {

    fun isMoreThanMinimum(amount: Map<Food, Int>): Boolean {
        val totalsMg = totalCalculator.getTotals(amount)
        return DailyMinimumIntake.minimumMgPerAmino.all { (amino, requiredMg) ->
            BigDecimal(requiredMg) <= totalsMg.getOrDefault(amino, BigDecimal.ZERO)
        }
    }

}