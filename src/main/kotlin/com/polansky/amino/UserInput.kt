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
 * User preferences: food constraints
 */
data class UserInput(
    val constraints: List<FoodAmountConstraint>,
)