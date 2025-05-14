package com.polansky.amino


class Chickpeas : Food() {
    override val name = "Chickpeas"
    override val histidineMgPer100g: Int
        get() = 566
    override val isoleucineMgPer100g: Int
        get() = 882
    override val leucineMgPer100g: Int
        get() = 1_460
    override val lysineMgPer100g: Int
        get() = 1_380
    override val methionineMgPer100g: Int
        get() = 270
    override val phenylalanineMgPer100g: Int
        get() = 1_100
    override val threonineMgPer100g: Int
        get() = 766
    override val tryptophanMgPer100g: Int
        get() = 200
    override val valineMgPer100g: Int
        get() = 865
    override val proteinMgPer100g: Int
        get() = 20_000


}