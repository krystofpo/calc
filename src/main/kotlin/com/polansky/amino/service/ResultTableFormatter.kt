package com.polansky.amino.service

import com.polansky.amino.AminoAcid
import com.polansky.amino.CombinationResult
import com.polansky.amino.DailyMinimumIntake
import com.polansky.amino.dto.CombinationTableDto
import com.polansky.amino.dto.FoodRowDto
import com.polansky.amino.dto.TotalsRowDto
import org.springframework.stereotype.Service
import kotlin.collections.set

@Service
class ResultTableFormatter {

    companion object {
        private val AMINO_ARRAY = AminoAcid.values()
        private val AMINO_NAMES: List<String> = AMINO_ARRAY.map { it.name }
        private val RDA_MG_BY_NAME: Map<String, Double> = LinkedHashMap<String, Double>(AMINO_ARRAY.size).apply {
            AMINO_ARRAY.forEach { aa ->
                this[aa.name] = DailyMinimumIntake.minimumMgPerAmino[aa] ?: 0.0
            }
        }
    }

    fun toTableDto(cr: CombinationResult): CombinationTableDto {
        // Accumulate totals in mg, then convert to grams for the DTO.
        val totalsMg = DoubleArray(AMINO_ARRAY.size)

        val rows = cr.gramsByFood.map { (food, gramsInt) ->
            val grams = gramsInt.toDouble()
            val gramsByAmino = LinkedHashMap<String, Double>(AMINO_ARRAY.size)
            var i = 0
            while (i < AMINO_ARRAY.size) {
                val aa = AMINO_ARRAY[i]
                val mgPer100 = food.mgPer100gOf(aa).toDouble()
                val totalMg = mgPer100 * grams / 100.0
                val totalG = totalMg / 1000.0
                gramsByAmino[AMINO_NAMES[i]] = totalG
                totalsMg[i] += totalMg
                i++
            }
            FoodRowDto(
                id = food.id.name,
                name = food.name,
                grams = grams,
                gramsByAmino = gramsByAmino
            )
        }

        val totalGramsByAmino = LinkedHashMap<String, Double>(AMINO_ARRAY.size)
        val percentRdaByAmino = LinkedHashMap<String, Double>(AMINO_ARRAY.size)
        var i = 0
        while (i < AMINO_ARRAY.size) {
            val name = AMINO_NAMES[i]
            val mg = totalsMg[i]
            totalGramsByAmino[name] = mg / 1000.0
            val rdaMg = RDA_MG_BY_NAME[name] ?: 0.0
            percentRdaByAmino[name] = if (rdaMg > 0.0) (mg / rdaMg) * 100.0 else 0.0
            i++
        }

        val totalGrams = cr.gramsByFood.values.sum().toDouble()

        return CombinationTableDto(
            aminoColumns = AMINO_NAMES,
            rows = rows,
            totals = TotalsRowDto(
                totalGrams = totalGrams,
                totalGramsByAmino = totalGramsByAmino,
                percentRdaByAmino = percentRdaByAmino
            )
        )
    }

}