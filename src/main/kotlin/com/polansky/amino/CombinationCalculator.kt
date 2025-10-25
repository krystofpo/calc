package com.polansky.amino

import com.polansky.amino.food.Food
import com.polansky.amino.service.TotalsCalculator
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

val STEP_GRAMS = 5
val MAX_COMBOS = 1_000 //TODO asi zmensit ? nebo prohledat cely stavovy prostor?
val MAX_RESULTS = 5
val DEFAULT_MAX_AMOUNT: Int = 100 //TODO

val MAX_DURATION = TimeUnit.SECONDS.toMillis(50000)//TODO jak vypnout behem debugu? apnout na produkci, lokalne, atd, otestovat to ze to funguje.

/**
 * Iterates combinations of foods (treated like an odometer based on the list
 * of FoodAmountConstraint) to find ones meeting the daily minima.
 */
@Service
class CombinationCalculator(val minimumCalculator: MinimumCalculator,
    val totalsCalculator: TotalsCalculator) {

    fun findCombinations(input: UserInput): List<CombinationResult> {//TODO refactorovat
        val startTime = System.currentTimeMillis()
        val constraints = input.constraints.associate { it.food to Pair(it.minGrams, it.maxGrams) }
        if (constraints.isEmpty()) return emptyList()

        val maxGrams = constraints.map { it.key to it.value.second }.toMap()
        if (!minimumCalculator.isMoreThanMinimum(maxGrams)) {
            val littleAminoMessage = getlittleAminoMessage(maxGrams)
            throw LittleAminoException(littleAminoMessage)
        }

        val results = mutableListOf<CombinationResult>()
        val currentGrams = constraints.entries.map { Amount(it.key, it.value.first) }

        while (true) {
            if (minimumCalculator.isMoreThanMinimum(currentGrams.associate { it.food to it.amount})) {
                results += CombinationResult(currentGrams.associate { it.food to it.amount })
                if (results.size >= MAX_COMBOS) {
                    break
                }

                if (System.currentTimeMillis() - startTime >= MAX_DURATION) {
                    log.warn { "TIMEOUT" }
                    break
                }
            }
            if (!incrementFoodOdometer(currentGrams, constraints)) break
        }
        val lowestWeightResults = getLowestWeightResults(results, MAX_RESULTS)
        return lowestWeightResults
    }

    private fun getlittleAminoMessage(amount: Map<Food, Int>): String { //TODO duplicate code with meets Minimum
        val totalsMg = totalsCalculator.getTotals(amount)
        val sb = StringBuilder("These aminos are insufficient: ") //TODO in a separate service, maybe use the table result formatter?
        // Compare to hardcoded minimums
        DailyMinimumIntake.minimumMgPerAmino.forEach({ (amino, requiredMg) ->
            run {
                val actualMg=totalsMg.getOrDefault(amino, BigDecimal.ZERO)
                if (actualMg < BigDecimal(requiredMg)) sb.append("$amino: actual: $actualMg mg, required: $requiredMg mg; ")
            }
        })
return sb.toString()
    }

    private fun getLowestWeightResults(results: MutableList<CombinationResult>, maxResults: Int)=
        results
            .sortedBy {calculateWeight(it)  }
            .take(maxResults)

    private fun calculateWeight(result: CombinationResult): Int {
val weight = result.gramsByFood.values.sum()
        return weight
    }


    private fun incrementFoodOdometer( //TODO prejmneovat nazancit co to vraci za hodnotu
        amountInGrams: List<Amount>,
        constraints: Map<Food, Pair<Int, Int>>
    ): Boolean {
        var odometerWasIncemented = false
        for (i in amountInGrams.indices.reversed()){
            val entry = amountInGrams[i]
            val max = constraints[entry.food]?.second?: DEFAULT_MAX_AMOUNT
            if ((entry.amount + STEP_GRAMS) <= max) {
                entry.amount += STEP_GRAMS
                // Reset lower-order dims to minimum
                for (j in i + 1 until amountInGrams.size) {
                    val food = amountInGrams[j].food
                   amountInGrams.get(j).amount = constraints[food]?.first?: 0 //TODO
                }
                odometerWasIncemented = true
                return odometerWasIncemented
            }
        }
        return odometerWasIncemented
    }
}

class LittleAminoException(message: String) : Exception(message)

class Amount(val food: Food, var amount: Int)

private val log = KotlinLogging.logger {  }