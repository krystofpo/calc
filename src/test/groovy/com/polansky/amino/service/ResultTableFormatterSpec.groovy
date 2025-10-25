package com.polansky.amino.service

import com.polansky.amino.AminoAcid
import com.polansky.amino.CombinationResult
import com.polansky.amino.DailyMinimumIntake
import com.polansky.amino.food.Oats
import com.polansky.amino.food.Rice
import spock.lang.Specification
import spock.lang.Subject

class ResultTableFormatterSpec extends Specification {

    @Subject
    ResultTableFormatter formatter = new ResultTableFormatter()

    def "below-RDA scenario: at least one amino is below its minimum"() {
        given: "A tiny serving that is certainly below RDA for several aminos"
        def rice = new Rice()
        def combo = new CombinationResult([(rice): 10]) // 10 g

        when:
        def dto = formatter.toTableDto(combo)

        then: "amino columns contain all AA names in order (including PROTEIN)"
        dto.aminoColumns == AminoAcid.values()*.name()

        and: "RDA map matches DailyMinimumIntake by name"
        dto.rdaMgByAmino.size() == AminoAcid.values().length
        AminoAcid.values().each { aa ->
            assert dto.rdaMgByAmino[aa.name()] == DailyMinimumIntake.minimumMgPerAmino[aa]
        }

        and: "Single row for rice has correct mg per amino (mgPer100g * grams / 100.0)"
        dto.rows.size() == 1
        def row = dto.rows[0]
        row.id == rice.id.name()
        row.name == rice.name
        row.grams == 10
        AminoAcid.values().each { aa ->
            def expected = rice.mgPer100g[aa] * 10 / 100.0
            assert Math.abs(row.mgByAmino[aa.name()] - expected) < 1e-9
        }

        and: "Totals equal the row values for each amino"
        AminoAcid.values().each { aa ->
            def expected = rice.mgPer100g[aa] * 10 / 100.0
            assert Math.abs(dto.totals.totalMgByAmino[aa.name()] - expected) < 1e-9
        }

        and: "At least one amino total is below its RDA"
        AminoAcid.values().any { aa ->
            dto.totals.totalMgByAmino[aa.name()] < dto.rdaMgByAmino[aa.name()]
        }
    }

    def "above-RDA scenario: all aminos meet or exceed RDA"() {
        given: "A large serving that exceeds all minima across the board"
        def oats = new Oats()
        def combo = new CombinationResult([(oats): 500]) // 500 g

        when:
        def dto = formatter.toTableDto(combo)

        then: "amino columns contain all AA names in order"
        dto.aminoColumns == AminoAcid.values()*.name()

        and: "All totals are >= RDA minima"
        AminoAcid.values().each { aa ->
            def total = dto.totals.totalMgByAmino[aa.name()]
            def rda = dto.rdaMgByAmino[aa.name()]
            assert total >= rda
        }

        and: "Row values and totals are consistent with calculation"
        def row = dto.rows[0]
        row.id == oats.id.name()
        row.name == oats.name
        row.grams == 500
        AminoAcid.values().each { aa ->
            def expected = oats.mgPer100g[aa] * 500 / 100.0
            assert Math.abs(row.mgByAmino[aa.name()] - expected) < 1e-9
            assert Math.abs(dto.totals.totalMgByAmino[aa.name()] - expected) < 1e-9
        }
    }
}