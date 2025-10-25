package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Sunflower : Food() {
    override val id = FoodName.SUNFLOWER
    override val name = "sunflower seed"
    override val link = "https://fdc.nal.usda.gov/food-details/170562/nutrients"
    override val histidineMgPer100g = 632
    override val isoleucineMgPer100g = 1100
    override val leucineMgPer100g = 1680
    override val lysineMgPer100g = 937
    override val methionineMgPer100g = 494
    override val phenylalanineMgPer100g = 1100
    override val threonineMgPer100g = 928
    override val tryptophanMgPer100g = 348
    override val valineMgPer100g = 1320
    override val proteinMgPer100g = 20_800

}
