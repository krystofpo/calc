package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Pumpkin : Food() {
    override val id = FoodName.PUMPKIN
    override val name = "pumpkin seed"
    override val link = "https://fdc.nal.usda.gov/food-details/170556/nutrients"
    override val histidineMgPer100g = 780
    override val isoleucineMgPer100g = 1200
    override val leucineMgPer100g = 2400
    override val lysineMgPer100g = 1200
    override val methionineMgPer100g = 600
    override val phenylalanineMgPer100g = 1700
    override val threonineMgPer100g = 1000
    override val tryptophanMgPer100g = 570
    override val valineMgPer100g = 1520
    override val proteinMgPer100g = 30_200

}
