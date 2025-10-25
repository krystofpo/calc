package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Oats : Food() {
    override val id = FoodName.OATS
    override val name = "Oats"
    override val link = ""
    override val histidineMgPer100g = 275
    override val isoleucineMgPer100g = 503
    override val leucineMgPer100g = 980
    override val lysineMgPer100g = 637
    override val methionineMgPer100g = 207
    override val phenylalanineMgPer100g = 665
    override val threonineMgPer100g = 382
    override val tryptophanMgPer100g = 182
    override val valineMgPer100g = 688
    override val proteinMgPer100g = 13_000

}