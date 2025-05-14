package com.polansky.amino

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Iterates combinations of foods (treated like an odometer based on the list
 * of FoodAmountConstraint) to find ones meeting the daily minima.
 */
class CombinationCalculator {

    fun findCombinations(input: UserInput): List<CombinationResult> {
        val constraints = input.constraints
        if (constraints.isEmpty()) return emptyList()

        // Early check: if all foods at maxGrams can’t meet minimums, exit.
        val maxGrams = constraints.map { it.maxGrams }.toIntArray()
        if (!meetsDailyMinimums(maxGrams, constraints)) return emptyList()

        val results = mutableListOf<CombinationResult>()
        val currentGrams = constraints.map { it.minGrams }.toIntArray()

        while (true) {
            if (meetsDailyMinimums(currentGrams, constraints)) {
                val map = constraints
                    .mapIndexed { i, c -> c.food to currentGrams[i] }
                    .toMap()
                results += CombinationResult(map)
                if (results.size >= input.maxResults) break
            }
            if (!incrementFoodOdometer(currentGrams, constraints)) break
        }
log.info { results }
        return results
    }
    /**
     * Sums amino acids for given grams and checks against daily minimums.
     */
    private fun meetsDailyMinimums(
        gramsArray: IntArray,
        constraints: List<FoodAmountConstraint>
    ): Boolean {
        val totalsMg = mutableMapOf<AminoAcid, Double>()
        gramsArray.forEachIndexed { idx, grams ->
            val food = constraints[idx].food
            food.mgPer100g.forEach { (amino, mgPer100g) ->
                val mg = mgPer100g * grams / 100.0
                totalsMg[amino] = totalsMg.getOrDefault(amino, 0.0) + mg
            }
        }
        // Compare to hardcoded minimums
        DailyMinimumIntake.minimumMgPerAmino.forEach { (amino, requiredMg) ->
            if (totalsMg.getOrDefault(amino, 0.0) < requiredMg) return false
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
            if (gramsArray[i] < max) {
                gramsArray[i]++
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

private val log = KotlinLogging.logger {  }