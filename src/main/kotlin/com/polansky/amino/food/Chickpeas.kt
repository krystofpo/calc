package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Chickpeas : Food() {
    override val id = FoodName.CHICKPEAS
    override val name = "Chickpeas"
    override val link = "https://fdc.nal.usda.gov/food-details/173756/nutrients"
    override val histidineMgPer100g = 566
    override val isoleucineMgPer100g = 882
    override val leucineMgPer100g = 1_460
    override val lysineMgPer100g = 1_380
    override val methionineMgPer100g = 270
    override val phenylalanineMgPer100g = 1_100
    override val threonineMgPer100g = 766
    override val tryptophanMgPer100g = 200
    override val valineMgPer100g = 865
    override val proteinMgPer100g = 20_000


}