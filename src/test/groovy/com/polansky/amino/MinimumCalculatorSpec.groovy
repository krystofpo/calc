package com.polansky.amino

import com.polansky.amino.food.Food
import com.polansky.amino.food.Rice
import com.polansky.amino.service.TotalsCalculator
import spock.lang.Specification
import spock.lang.Subject

import java.math.BigDecimal

class MinimumCalculatorSpec extends Specification {

    TotalsCalculator totalsCalculator = Mock()

    @Subject
    MinimumCalculator minimumCalculator = new MinimumCalculator(totalsCalculator)

    def "delegates the provided grams map to TotalsCalculator"() {
        given:
        Map<Food, Integer> gramsByFood = [(new Rice()): 200]

        when:
        minimumCalculator.isMoreThanMinimum(gramsByFood)

        then:
        1 * totalsCalculator.getTotals(gramsByFood) >> [:]
    }


    def "returns true when all amounts meet or exceed daily minima (boundary equals)"() {
        given:
        // Build an input map where every amino amount equals the configured minimum
        Map<AminoAcid, BigDecimal> amounts = DailyMinimumIntake.minimumMgPerAmino.collectEntries { aa, min ->
            [(aa): BigDecimal.valueOf(min)]
        }

        totalsCalculator.getTotals(_) >> amounts

        expect:
        minimumCalculator.isMoreThanMinimum([:])
    }

    def "returns false when at least one amino is below the minimum"() {
        given:
        // Start with all at minimum
        Map<AminoAcid, BigDecimal> amounts = DailyMinimumIntake.minimumMgPerAmino.collectEntries { aa, min ->
            [(aa): BigDecimal.valueOf(min)]
        }
        // Lower one amino acid just below the minimum
        def anyAmino = AminoAcid.HISTIDINE
        def minValue = DailyMinimumIntake.minimumMgPerAmino[anyAmino]
        amounts[anyAmino] = BigDecimal.valueOf(minValue).subtract(BigDecimal.ONE)

        totalsCalculator.getTotals(_) >> amounts

        expect:
        !minimumCalculator.isMoreThanMinimum([ : ])
    }

    def "returns false when a required amino acid is missing (defaults to zero)"() {
        given:
        // Provide a map missing one amino acid key
        Map<AminoAcid, BigDecimal> amounts = DailyMinimumIntake.minimumMgPerAmino
                .findAll { aa, _ -> aa != AminoAcid.VALINE }
                .collectEntries { aa, min -> [(aa): BigDecimal.valueOf(min)] }

        totalsCalculator.getTotals(_) >> amounts

        expect:
        !minimumCalculator.isMoreThanMinimum([ : ])
    }
}
