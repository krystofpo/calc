package com.polansky.amino.service

import com.polansky.amino.AminoAcid
import com.polansky.amino.CombinationResult
import com.polansky.amino.food.Oats
import com.polansky.amino.food.Rice
import spock.lang.Specification
import spock.lang.Subject

class ResultTableFormatterSpec extends Specification {

    @Subject
    ResultTableFormatter formatter = new ResultTableFormatter()

    def "below-RDA scenario: at least one amino is below its minimum (percent < 100)"() {
        given:
        def rice = new Rice()
        def combo = new CombinationResult([(rice): 10]) // 10 g

        when:
        def dto = formatter.toTableDto(combo)

        then: "amino columns contain all AA names in order (including PROTEIN)"
        dto.aminoColumns == AminoAcid.values()*.name()

        and: "Single row for rice has correct grams per amino (mgPer100g * grams / 100 / 1000)"
        dto.rows.size() == 1
        def row = dto.rows[0]
        row.id == rice.id.name()
        row.name == rice.name
        row.grams == 10.0d
        AminoAcid.values().each { aa ->
            def expectedMg = rice.mgPer100g[aa] * 10 / 100.0
            def expectedG = expectedMg / 1000.0
            assert Math.abs(row.gramsByAmino[aa.name()] - expectedG) < 1e-9
        }

        and: "Totals equal the row values (in grams) and percentRdaByAmino reflects <100% for at least one amino"
        AminoAcid.values().any { aa ->
            (dto.totals.percentRdaByAmino[aa.name()] ?: 0.0) < 100.0
        }
        AminoAcid.values().each { aa ->
            def expectedMg = rice.mgPer100g[aa] * 10 / 100.0
            def expectedG = expectedMg / 1000.0
            assert Math.abs(dto.totals.totalGramsByAmino[aa.name()] - expectedG) < 1e-9
        }
        dto.totals.totalGrams == 10.0d
    }

    def "above-RDA scenario: all aminos meet or exceed RDA (percent >= 100)"() {
        given:
        def oats = new Oats()
        def combo = new CombinationResult([(oats): 500]) // 500 g

        when:
        def dto = formatter.toTableDto(combo)

        then: "amino columns contain all AA names in order"
        dto.aminoColumns == AminoAcid.values()*.name()

        and: "All percent values are >= 100 and totals match computed grams"
        def row = dto.rows[0]
        row.id == oats.id.name()
        row.name == oats.name
        row.grams == 500.0d
        AminoAcid.values().each { aa ->
            def expectedMg = oats.mgPer100g[aa] * 500 / 100.0
            def expectedG = expectedMg / 1000.0
            assert Math.abs(row.gramsByAmino[aa.name()] - expectedG) < 1e-9
            assert Math.abs(dto.totals.totalGramsByAmino[aa.name()] - expectedG) < 1e-9
            assert (dto.totals.percentRdaByAmino[aa.name()] ?: 0.0) >= 100.0
        }
        dto.totals.totalGrams == 500.0d
    }
}