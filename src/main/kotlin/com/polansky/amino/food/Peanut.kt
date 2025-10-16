package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Peanut : Food() {
    override val id = 9L
    override val name = "peanut"
    override val link = "https://fdc.nal.usda.gov/food-details/172430/nutrients"
    override val histidineMgPer100g = 652
    override val isoleucineMgPer100g = 900
    override val leucineMgPer100g = 1600
    override val lysineMgPer100g = 900
    override val methionineMgPer100g = 300
    override val phenylalanineMgPer100g = 1300
    override val threonineMgPer100g = 800
    override val tryptophanMgPer100g = 250
    override val valineMgPer100g = 1000
    override val proteinMgPer100g = 25_800

}
