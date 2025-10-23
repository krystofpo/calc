package com.polansky.amino.app

import com.polansky.amino.*
import com.polansky.amino.controller.CalcFoodDto
import com.polansky.amino.controller.CalculateDto
import com.polansky.amino.controller.FoodDto
import com.polansky.amino.food.Food
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AppService(
    val allFoods: List<Food>,
    val combinationCalculator: CombinationCalculator
) {

    companion object {
        private val AMINO_ARRAY = AminoAcid.values()
        private val AMINO_NAMES: List<String> = AMINO_ARRAY.map { it.name }
        private val RDA_MAP: Map<String, Double> = LinkedHashMap<String, Double>(AMINO_ARRAY.size).apply {
            AMINO_ARRAY.forEach { aa ->
                this[aa.name] = DailyMinimumIntake.minimumMgPerAmino[aa] ?: 0.0
            }
        }
    }

    fun calculate(calculateDto: CalculateDto): List<ResultDto> {
        val userInput = getUserInput(calculateDto)
        val combinations = combinationCalculator.findCombinations(userInput)
        return combinations.map { toResultDto(it) }
    }

    /**
     * New: produce table-shaped results optimized for frontend rendering
     * Rows = foods, Columns = amino acids (including PROTEIN), Totals = sum mg per amino.
     * If no combination is found or an error occurs, returns a single table built from max grams as a diagnostic.
     */
    fun calculateTable(calculateDto: CalculateDto): List<CombinationTableDto> {
        return try {
            val userInput = getUserInput(calculateDto)
            val combinations = combinationCalculator.findCombinations(userInput)
            if (combinations.isNotEmpty()) {
                combinations.map { toTableDto(it) }
            } else {
                val table = toTableDto(fromMaxCombination(calculateDto))
                val msg = "Even when we use the maximum amount for each selected food, the totals do not reach the minimum daily values. Try increasing amounts or adding different foods."
                listOf(table.copy(errorMessage = msg))
            }
        } catch (ex: Exception) {
            val table = toTableDto(fromMaxCombination(calculateDto))
            val msg = "Even when we use the maximum amount for each selected food, the totals do not reach the minimum daily values. Try increasing amounts or adding different foods."
            listOf(table.copy(errorMessage = msg))
        }
    }

    private fun getUserInput(calculateDto: CalculateDto): UserInput {
        return UserInput(
            calculateDto.foods.map { toConstraint(it) },
            5
        )
    }

    private fun toConstraint(calcFoodDto: CalcFoodDto): FoodAmountConstraint {
        return calcFoodDto.let {
            FoodAmountConstraint(findFood(it), it.min, it.max)
        }
    }

    private fun findFood(calcFoodDto: CalcFoodDto): Food {
        return allFoods.first { it.id == calcFoodDto.id }
    }

    private fun toResultDto(cr: CombinationResult): ResultDto {
        // 1) Foods list
        val foods = cr.gramsByFood.map { (food, grams) ->
            FoodResultDto(
                id = food.id,
                name = food.name,
                amount = grams
            )
        }

        // 2) Amino acids: total mg → g, then % of RDA (both in mg)
        val aminoAcids = cr.totalMgByAmino().map { (amino, totalMg) ->
            // totalMg is a Double in mg
            val totalG = BigDecimal.valueOf(totalMg)
                .divide(BigDecimal.valueOf(1_000), 2, RoundingMode.HALF_UP)

            // lookup RDA in mg
            val rdaMg = BigDecimal.valueOf(
                DailyMinimumIntake.minimumMgPerAmino[amino]
                    ?: error("Missing RDA for $amino")
            )

            // percent = totalMg / rdaMg * 100
            val rdaPercent = if (rdaMg > BigDecimal.ZERO) {
                BigDecimal.valueOf(totalMg)
                    .divide(rdaMg, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
            } else {
                BigDecimal.ZERO
            }

            AminoResultDto(
                name = amino.name,
                total = totalG,
                rdaPercent = rdaPercent
            )
        }

        return ResultDto(foods = foods, aminoAcids = aminoAcids)
    }

    private fun toTableDto(cr: CombinationResult): CombinationTableDto {
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
                id = food.id,
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

    /**
     * Build a diagnostic combination using each food's maximum grams.
     * This allows frontend to reuse the same table when no valid combination exists and explain shortfalls vs. daily minimums.
     */
    private fun fromMaxCombination(calculateDto: CalculateDto): CombinationResult {
        val gramsByFood = calculateDto.foods.associate { dto ->
            findFood(dto) to dto.max
        }
        return CombinationResult(gramsByFood)
    }

    fun suggest(suggest: String): List<FoodDto> {
        return allFoods.filter { it.name.contains(suggest, true) }
            .map { FoodDto(it.id, it.name) }
    }

}

class ResultDto(
    val foods: List<FoodResultDto>,
    val aminoAcids: List<AminoResultDto>
)

class AminoResultDto(
    val name: String,
    val total: BigDecimal,
    val rdaPercent: BigDecimal
)

class FoodResultDto(
    val id: Long,
    val name: String,
    val amount: Int
)


