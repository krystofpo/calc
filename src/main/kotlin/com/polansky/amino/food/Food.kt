package com.polansky.amino.food

import com.polansky.amino.AminoAcid
import com.polansky.amino.AminoAcid.*

/**
 * Abstract base for all foods. Enforces compile-time definition of every amino acid.
 * Subclasses must override each amino's mg per 100g.
 */
abstract class Food {
    abstract val id: FoodName
    abstract val name: String
    abstract val link: String
    abstract val histidineMgPer100g: Int
    abstract val isoleucineMgPer100g: Int
    abstract val leucineMgPer100g: Int
    abstract val lysineMgPer100g: Int
    abstract val methionineMgPer100g: Int
    abstract val phenylalanineMgPer100g: Int
    abstract val threonineMgPer100g: Int
    abstract val tryptophanMgPer100g: Int
    abstract val valineMgPer100g: Int
    abstract val proteinMgPer100g: Int

    /**
     * Provides a standard map from amino acids to mg per 100g,
     * built from the abstract properties above.
     */
    val mgPer100g: Map<AminoAcid, Int>
        get() = mapOf(
            HISTIDINE to histidineMgPer100g,
            ISOLEUCINE to isoleucineMgPer100g,
            LEUCINE to leucineMgPer100g,
            LYSINE to lysineMgPer100g,
            METHIONINE to methionineMgPer100g,
            PHENYLALANINE to phenylalanineMgPer100g,
            THREONINE to threonineMgPer100g,
            TRYPTOPHAN to tryptophanMgPer100g,
            VALINE to valineMgPer100g,
            PROTEIN to proteinMgPer100g
        )

    /**
     * Faster accessor to avoid allocating the map for each lookup.
     */
    fun mgPer100gOf(aa: AminoAcid): Int = when (aa) {
        HISTIDINE -> histidineMgPer100g
        ISOLEUCINE -> isoleucineMgPer100g
        LEUCINE -> leucineMgPer100g
        LYSINE -> lysineMgPer100g
        METHIONINE -> methionineMgPer100g
        PHENYLALANINE -> phenylalanineMgPer100g
        THREONINE -> threonineMgPer100g
        TRYPTOPHAN -> tryptophanMgPer100g
        VALINE -> valineMgPer100g
        PROTEIN -> proteinMgPer100g
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Food) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}