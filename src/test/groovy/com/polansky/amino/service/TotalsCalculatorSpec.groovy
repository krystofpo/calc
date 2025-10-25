package com.polansky.amino.service

import com.polansky.amino.AminoAcid
import com.polansky.amino.FoodAmountConstraint
import com.polansky.amino.food.Oats
import com.polansky.amino.food.Rice
import spock.lang.Specification
import spock.lang.Subject

class TotalsCalculatorSpec extends Specification {

    @Subject
    TotalsCalculator totalsCalculator = new TotalsCalculator()

    def "returns exact mg totals for a single food at 100g"() {
        given:
        def rice = new Rice()

        when:
        def totals = totalsCalculator.getTotals([(rice): 100])

        then: "all amino acids match the food's mgPer100g values as BigDecimal"
        totals.size() == AminoAcid.values().length
        AminoAcid.values().each { aa ->
            assert totals[aa] == BigDecimal.valueOf(rice.mgPer100g.get(aa))
        }
    }

    def "sums mg totals across multiple foods (100g each)"() {
        given:
        def rice = new Rice()
        def oats = new Oats()

        when:
        def totals = totalsCalculator.getTotals([(rice): 100, (oats) : 100])

        then: "totals equal per-100g values summed across foods"
        AminoAcid.values().each { aa ->
            def expected = rice.mgPer100g.get(aa) + oats.mgPer100g.get(aa)
            assert totals[aa] == BigDecimal.valueOf(expected)
        }
    }

    def "uses integer division (truncates) when grams are not a multiple of 100"() {
        given:
        def rice = new Rice()

        when:
        def totals = totalsCalculator.getTotals([(rice):15])

        then:
        AminoAcid.values().each { aa ->
            def per100 = rice.mgPer100g.get(aa)
            def expected = Math.floorDiv(per100 * 15, 100)
            assert totals[aa] == BigDecimal.valueOf(expected)
        }
    }
}
