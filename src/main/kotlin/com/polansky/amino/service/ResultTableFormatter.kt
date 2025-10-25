package com.polansky.amino.service

import com.polansky.amino.AminoAcid
import com.polansky.amino.CombinationResult
import com.polansky.amino.DailyMinimumIntake
import com.polansky.amino.dto.CombinationTableDto
import com.polansky.amino.dto.FoodRowDto
import com.polansky.amino.dto.TotalsRowDto
import org.springframework.stereotype.Service
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

@Service
class ResultTableFormatter {

    companion object {
        private val AMINO_ARRAY = AminoAcid.values()
        private val AMINO_NAMES: List<String> = AMINO_ARRAY.map { it.name }
        private val RDA_MAP: Map<String, Double> = LinkedHashMap<String, Double>(AMINO_ARRAY.size).apply {
            AMINO_ARRAY.forEach { aa ->
                this[aa.name] = DailyMinimumIntake.minimumMgPerAmino[aa] ?: 0.0
            }
        }
    }

    fun toTableDto(cr: CombinationResult): CombinationTableDto {
        // Build rows and totals in a single pass; reuse cached amino arrays and RDA map.
        val totalsArray = DoubleArray(AMINO_ARRAY.size) // primitive array to avoid boxing during accumulation

        val rows = cr.gramsByFood.map { (food, grams) ->
            val mgByAmino = LinkedHashMap<String, Double>(AMINO_ARRAY.size)
            var i = 0
            while (i < AMINO_ARRAY.size) {
                val aa = AMINO_ARRAY[i]
                val mgPer100 = food.mgPer100gOf(aa).toDouble()
                val totalMg = mgPer100 * grams / 100.0
                mgByAmino[AMINO_NAMES[i]] = totalMg
                totalsArray[i] += totalMg
                i++
            }
            FoodRowDto(
                id = food.id.name,
                name = food.name,
                grams = grams,
                mgByAmino = mgByAmino
            )
        }

        val totalsMap = LinkedHashMap<String, Double>(AMINO_ARRAY.size)
        var i = 0
        while (i < AMINO_ARRAY.size) {
            totalsMap[AMINO_NAMES[i]] = totalsArray[i]
            i++
        }

        return CombinationTableDto(
            aminoColumns = AMINO_NAMES,
            rows = rows,
            totals = TotalsRowDto(totalMgByAmino = totalsMap),
            rdaMgByAmino = RDA_MAP
        )
    }

}