package com.polansky.amino

import com.polansky.amino.food.Food


/**
 * Defines minimum and maximum food intake, in grams (g).
 */
data class FoodAmountConstraint(
    val food: Food,
    val minGrams: Int,
    val maxGrams: Int
)

/**
 * User preferences: food constraints and how many combinations to find.
 */
data class UserInput(
    val constraints: List<FoodAmountConstraint>,
    val maxResults: Int = 5
)