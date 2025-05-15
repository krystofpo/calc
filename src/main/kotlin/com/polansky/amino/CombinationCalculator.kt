package com.polansky.amino

import io.github.oshai.kotlinlogging.KotlinLogging
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

val STEP_GRAMS = 5
val MAX_RESULTS = 10_000
val MAX_DURATION = TimeUnit.SECONDS.toMillis(15)

/**
 * Iterates combinations of foods (treated like an odometer based on the list
 * of FoodAmountConstraint) to find ones meeting the daily minima.
 */
class CombinationCalculator {

    fun findCombinations(input: UserInput): List<CombinationResult> {
        val startTime = System.currentTimeMillis()
        val constraints = input.constraints
        if (constraints.isEmpty()) return emptyList()

        // Early check: if all foods at maxGrams can’t meet minimums, exit.
        val maxGrams = constraints.map { it.maxGrams }.toIntArray()
        if (!meetsDailyMinimums(maxGrams, constraints)) {
            val littleAminoMessage = getlittleAminoMessage(maxGrams, constraints)
            throw LittleAminoException(littleAminoMessage)
        }

        val results = mutableListOf<CombinationResult>()
        val currentGrams = constraints.map { it.minGrams }.toIntArray()

        while (true) {
            if (meetsDailyMinimums(currentGrams, constraints)) {
                val map = constraints
                    .mapIndexed { i, c -> c.food to currentGrams[i] }
                    .toMap()
                results += CombinationResult(map)
                if (results.size >= MAX_RESULTS) {
                    log.warn { "MAX results reached" }
                    break
                }
                if (System.currentTimeMillis() - startTime >= MAX_DURATION) {
                    log.warn { "TIMEOUT" }
                    break
                }
            }
            if (!incrementFoodOdometer(currentGrams, constraints)) break
        }
        log.info { "Found ${results.size} results" }
        val lowestWeightResults = getLowestWeightResults(results, input.maxResults)
log.info { lowestWeightResults }
        return lowestWeightResults
    }

    private fun getlittleAminoMessage(gramsArray: IntArray, constraints: List<FoodAmountConstraint>): String { //TODO duplicate code with meets Minimum
        val totalsMg = getTotals(gramsArray, constraints)
        val sb = StringBuilder("These aminos are insufficient: ")
        // Compare to hardcoded minimums
        DailyMinimumIntake.minimumMgPerAmino.forEach({ (amino, requiredMg) ->
            run {
                val actualMg=totalsMg.getOrDefault(amino, BigDecimal.ZERO)
                if (actualMg < BigDecimal(requiredMg)) sb.append("$amino: actual: $actualMg mg, required: $requiredMg mg; ")
            }
        })
return sb.toString()
    }

    private fun getTotals(
        gramsArray: IntArray,
        constraints: List<FoodAmountConstraint>
    ): MutableMap<AminoAcid, BigDecimal> {
        val totalsMg = mutableMapOf<AminoAcid, BigDecimal>()
        gramsArray.forEachIndexed { idx, grams ->
            val food = constraints[idx].food
            food.mgPer100g.forEach { (amino, mgPer100g) ->
                val mg = BigDecimal(mgPer100g * grams).divideToIntegralValue(BigDecimal(100))
                totalsMg[amino] = totalsMg.getOrDefault(amino, BigDecimal.ZERO).add(mg)
            }
        }
        return totalsMg
    }

    private fun getLowestWeightResults(results: MutableList<CombinationResult>, maxResults: Int)=
        results
            .sortedBy {calculateWeight(it)  }
            .take(maxResults)

    private fun calculateWeight(result: CombinationResult): Int {
val weight = result.gramsByFood.values.sum()
        return weight
    }


    /**
     * Sums amino acids for given grams and checks against daily minimums.
     */
    private fun meetsDailyMinimums(
        gramsArray: IntArray,
        constraints: List<FoodAmountConstraint>
    ): Boolean {
        val totalsMg = getTotals(gramsArray, constraints)
        // Compare to hardcoded minimums
        DailyMinimumIntake.minimumMgPerAmino.forEach { (amino, requiredMg) ->
            if (totalsMg.getOrDefault(amino, BigDecimal.ZERO) < BigDecimal(requiredMg)) return false
        }
        return true
    }

    /**
     * Advances the gramsArray as an "odometer". Returns false when all at max.
     */
    private fun incrementFoodOdometer(
        gramsArray: IntArray,
        constraints: List<FoodAmountConstraint>
    ): Boolean {
        for (i in gramsArray.indices.reversed()) {
            val max = constraints[i].maxGrams
            if ((gramsArray[i] + STEP_GRAMS) < max) { //TODO nebo  <= ?
                gramsArray[i]+= STEP_GRAMS
                // Reset lower-order dims to minimum
                for (j in i + 1 until gramsArray.size) {
                    gramsArray[j] = constraints[j].minGrams
                }
                return true
            }
        }
        return false
    }
    // ... (meetsDailyMinimums and incrementFoodOdometer remain unchanged, indexing by food constraint list, so there's
    // no ambiguity: array index i always refers to constraints[i].)
}

class LittleAminoException(message: String) : Exception(message)

private val log = KotlinLogging.logger {  }