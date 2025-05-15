package com.polansky.amino

/**
 * Represents one valid food combination.
 * Stores grams (g) per food and computes total amino acids in mg.
 */
data class CombinationResult(
    val gramsByFood: Map<Food, Int>
) {
    /**
     * Computes total amino acid amounts in milligrams (mg) for this combination.
     */
    fun totalMgByAmino(): Map<AminoAcid, Double> {
        val totals = mutableMapOf<AminoAcid, Double>()
        gramsByFood.forEach { (food, grams) ->
            food.mgPer100g.forEach { (amino, mgPer100g) ->
                val mgFromFood = mgPer100g * grams / 100.0
                totals[amino] = totals.getOrDefault(amino, 0.0) + mgFromFood
            }
        }
        return totals
    }

    /**
     * Pretty-prints the combination in a concise table:
     * columns for Food, Grams, then each Amino acid with its mg/100g and total mg.
     */
    override fun toString(): String {
        //TODO add total of weight
        //TODO add table borders, like sql resultset
        //TODO cleanCode : aaList fuj co to je za nazev
        //TODO use bigdecimals, reuse logic from calculation
        val aaList = AminoAcid.values()
        val sb = StringBuilder()
        // Header: Food | g | for each amino: mg/100g and total mg
        sb.append(String.format("%-10s %6s", "Food", "g"))
        aaList.forEach { aa ->
            sb.append(String.format(" %10s %10s", "${aa.name}/100g", aa.name))
        }
        sb.append("\n")

                    // Rows: each food's grams, mg/100g and total mg
                    gramsByFood.forEach { (food, grams) ->
                sb.append(String.format("%-10s %6dg", food.name, grams))
                aaList.forEach { aa ->
                    val mgPer100 = food.mgPer100g[aa] ?: 0
                    val totalMg = mgPer100 * grams / 100.0
                    sb.append(String.format(" %10d %10.0f", mgPer100, totalMg))
                }
                sb.append("\n")
            }

            // Total row: blank mg/100g, then sum of total mg per amino
            val totals = totalMgByAmino()
        sb.append(String.format("%-10s %6s", "Total", ""))
        aaList.forEach { aa ->
            sb.append(String.format(" %10s %10.0f", "", totals[aa] ?: 0.0))
        }
        sb.append("\n")

            return sb.toString()
    }
}