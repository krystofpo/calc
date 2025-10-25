package com.polansky.amino.food

import com.polansky.amino.food.FoodName.BUCKWHEAT
import org.springframework.stereotype.Component

/**
 * Buckwheat groats, plain, raw (per 100 g, SR Legacy FDC ID 170286).
 */
@Component
class Buckwheat : Food() {
    override val id                        = BUCKWHEAT
    override val name                      = "Buckwheat groats raw"
    override val link = "https://fdc.nal.usda.gov/food-details/170286/nutrients"
    // Essential amino acids (mg per 100 g)
    override val histidineMgPer100g        = 309   // 0.309 g × 1000
    override val isoleucineMgPer100g       = 498   // 0.498 g × 1000
    override val leucineMgPer100g          = 832   // 0.832 g × 1000
    override val lysineMgPer100g           = 672   // 0.672 g × 1000
    override val methionineMgPer100g       = 172   // 0.172 g × 1000
    override val phenylalanineMgPer100g    = 520   // 0.520 g × 1000
    override val threonineMgPer100g        = 506   // 0.506 g × 1000
    override val tryptophanMgPer100g       = 192   // 0.192 g × 1000
    override val valineMgPer100g           = 678   // 0.678 g × 1000

    // Total protein (mg per 100 g)
    override val proteinMgPer100g          = 13_200 // 13.2 g × 1000
}
