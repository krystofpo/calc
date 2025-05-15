package com.polansky.amino.food

import org.springframework.stereotype.Component

@Component
class Rice : Food() {
    override val id = 4L
    override val name = "Rice"
    override val histidineMgPer100g = 168
    override val isoleucineMgPer100g = 308
    override val leucineMgPer100g = 589
    override val lysineMgPer100g = 258
    override val methionineMgPer100g = 168
    override val phenylalanineMgPer100g = 381
    override val threonineMgPer100g = 255
    override val tryptophanMgPer100g = 83
    override val valineMgPer100g = 435
    override val proteinMgPer100g = 6_000












}