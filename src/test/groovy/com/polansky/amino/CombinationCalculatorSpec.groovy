package com.polansky.amino

import spock.lang.Specification

class CombinationCalculatorSpec extends Specification {
    def "returns empty if maxGrams can’t meet minima"() {
        given:
        def food = new Rice() // Rice has 28000mg protein/100g => 280mg/g
        def input = new UserInput([
                new FoodAmountConstraint(food, 1, 1)
        ], 3)

        when:
        def combos = new CombinationCalculator().findCombinations(input)

        then:
        combos.isEmpty()
    }

    def "finds combos using concrete food classes"() {
        given:
        DailyMinimumIntake.minimumMgPerAmino = [(AminoAcid.PROTEIN): 300.0]
        def a = new Rice()
        def input = new UserInput([
                new FoodAmountConstraint(a, 1, 10)
        ], 2)

        when:
        def combos = new CombinationCalculator().findCombinations(input)

        then:
        combos.every { it.totalMgByAmino()[AminoAcid.PROTEIN] >= 300.0 }
    }

    def "finds combos using chickjepas"() {
        given:
        def a = new Chickpeas()
        def input = new UserInput([
                new FoodAmountConstraint(a, 1, 500)
        ], 2)

        when:
        def combos = new CombinationCalculator().findCombinations(input)

        then:
        combos.every { it.totalMgByAmino()[AminoAcid.PROTEIN] >= 70_000 }
    }
}