package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Soy : Food() {
    override val id = 11L
    override val name = "soy beans, mature seeds, dry roasted"
    override val link = "https://fdc.nal.usda.gov/food-details/172441/nutrients"
    override val histidineMgPer100g = 1000
    override val isoleucineMgPer100g = 1900
    override val leucineMgPer100g = 3200
    override val lysineMgPer100g = 2600
    override val methionineMgPer100g = 500
    override val phenylalanineMgPer100g = 2000
    override val threonineMgPer100g = 1700
    override val tryptophanMgPer100g = 570
    override val valineMgPer100g = 1900
    override val proteinMgPer100g = 43_000

}
