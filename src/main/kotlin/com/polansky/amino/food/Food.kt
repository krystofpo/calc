package com.polansky.amino.food

import com.polansky.amino.AminoAcid
import com.polansky.amino.AminoAcid.*

/**
 * Abstract base for all foods. Enforces compile-time definition of every amino acid.
 * Subclasses must override each amino's mg per 100g.
 */
abstract class Food {
    abstract val id: Long //TODO how to assert uniquness? entities? hashcode?
    abstract val name: String
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
}