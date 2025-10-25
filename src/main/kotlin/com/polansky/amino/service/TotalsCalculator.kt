package com.polansky.amino.service

import com.polansky.amino.AminoAcid
import com.polansky.amino.food.Food
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TotalsCalculator {

    fun getTotals(
        constraints: Map<Food, Int>,
    ): MutableMap<AminoAcid, BigDecimal> {
        val totalsMg = mutableMapOf<AminoAcid, BigDecimal>()
        constraints.forEach { entry ->
            val food = entry.key
            food.mgPer100g.forEach { (amino, mgPer100g) ->
                val mg = BigDecimal(mgPer100g * entry.value).divideToIntegralValue(BigDecimal(100))
                totalsMg[amino] = totalsMg.getOrDefault(amino, BigDecimal.ZERO).add(mg)
            }
        }
        return totalsMg
    }
}