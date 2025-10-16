package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Tofu : Food() {
    override val id = 10L
    override val name = "tofu"
    override val link = "https://fdc.nal.usda.gov/food-details/172475/nutrients , https://fdc.nal.usda.gov/food-details/174297/nutrients, https://fdc.nal.usda.gov/food-details/172448/nutrients"
    override val histidineMgPer100g = 225
    override val isoleucineMgPer100g = 444
    override val leucineMgPer100g = 728
    override val lysineMgPer100g = 462
    override val methionineMgPer100g = 110
    override val phenylalanineMgPer100g = 435
    override val threonineMgPer100g = 411
    override val tryptophanMgPer100g = 123
    override val valineMgPer100g = 455
    override val proteinMgPer100g = 9_000

}
