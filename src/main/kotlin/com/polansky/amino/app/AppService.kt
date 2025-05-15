package com.polansky.amino.app

import com.polansky.amino.*
import com.polansky.amino.controller.CalcFoodDto
import com.polansky.amino.controller.CalculateDto
import com.polansky.amino.food.Food
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class AppService(
    val allFoods: List<Food>,
    val combinationCalculator: CombinationCalculator
) {

    fun calculate(calculateDto: CalculateDto): List<ResultDto> {
        val userInput = getUserInput(calculateDto)
        val combinations = combinationCalculator.findCombinations(userInput)
        return combinations.map { toResultDto(it) }
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


