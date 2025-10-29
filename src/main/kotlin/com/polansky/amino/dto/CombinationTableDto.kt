package com.polansky.amino.dto

/**
 * DTO for rendering a single combination as a table.
 * - aminoColumns: order of amino acid columns (including PROTEIN)
 * - rows: one per food with grams and grams of each amino for that food's amount
 * - totals: total grams across all foods for each amino and percentage of RDA (as Double)
 * - errorMessage: optional banner to display when this is a diagnostic (fallback) result
 */
 data class CombinationTableDto(
     val aminoColumns: List<String>,
     val rows: List<FoodRowDto>,
     val totals: TotalsRowDto,
     val errorMessage: String? = null
 )
 
 data class FoodRowDto(
     val id: String,
     val name: String,
     val grams: Double,
     val gramsByAmino: Map<String, Double>
 )
 
 data class TotalsRowDto(
     val totalGrams: Double,
     val totalGramsByAmino: Map<String, Double>,
     val percentRdaByAmino: Map<String, Double>
 )
