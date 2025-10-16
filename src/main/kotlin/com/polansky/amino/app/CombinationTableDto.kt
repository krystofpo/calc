package com.polansky.amino.app

/**
 * DTO for rendering a single combination as a table.
 * - aminoColumns: order of amino acid columns (including PROTEIN)
 * - rows: one per food with grams and mg of each amino for that food's amount
 * - totals: total mg across all foods for each amino
 * - rdaMgByAmino: daily minimum intake per amino in mg (for percentage computation)
 * - errorMessage: optional banner to display when this is a diagnostic (fallback) result
 */
data class CombinationTableDto(
    val aminoColumns: List<String>,
    val rows: List<FoodRowDto>,
    val totals: TotalsRowDto,
    val rdaMgByAmino: Map<String, Double>,
    val errorMessage: String? = null
)

data class FoodRowDto(
    val id: Long,
    val name: String,
    val grams: Int,
    val mgByAmino: Map<String, Double>
)

data class TotalsRowDto(
    val totalMgByAmino: Map<String, Double>
)
