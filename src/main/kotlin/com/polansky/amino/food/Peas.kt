package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Peas : Food() {
    override val id = 6L
    override val name = "Peas, green, split, mature seeds, raw"
    override val link = "https://fdc.nal.usda.gov/fdc-app.html#/food-details/172428/nutrients"
    override val histidineMgPer100g = 586
    override val isoleucineMgPer100g = 983
    override val leucineMgPer100g = 1680
    override val lysineMgPer100g = 1700
    override val methionineMgPer100g = 195
    override val phenylalanineMgPer100g = 1100
    override val threonineMgPer100g = 813
    override val tryptophanMgPer100g = 159
    override val valineMgPer100g = 1045
    override val proteinMgPer100g = 23_100

}
