package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Eggs : Food() {
    override val id = FoodName.EGG
    override val name = "Eggs, raw (one egg=50g)"
    override val link = ""
    override val histidineMgPer100g =335
    override val isoleucineMgPer100g =728
    override val leucineMgPer100g =1_180
    override val lysineMgPer100g =989
    override val methionineMgPer100g =411
    override val phenylalanineMgPer100g =736
    override val threonineMgPer100g =602
    override val tryptophanMgPer100g =181
    override val valineMgPer100g =930
    override val proteinMgPer100g =13_000

}