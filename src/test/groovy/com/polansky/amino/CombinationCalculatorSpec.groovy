package com.polansky.amino

import com.polansky.amino.food.Buckwheat
import com.polansky.amino.food.Chickpeas
import com.polansky.amino.food.Eggs
import com.polansky.amino.food.Oats
import com.polansky.amino.food.Rice
import spock.lang.Specification

import static com.polansky.amino.AminoAcid.PROTEIN

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
        DailyMinimumIntake.minimumMgPerAmino = [(PROTEIN): 300.0]
        def a = new Rice()
        def input = new UserInput([
                new FoodAmountConstraint(a, 1, 10)
        ], 2)

        when:
        def combos = new CombinationCalculator().findCombinations(input)

        then:
        combos.every { it.totalMgByAmino()[PROTEIN] >= 300.0 }
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
        combos.every { it.totalMgByAmino()[PROTEIN] >= 70_000 }
    }

    def "finds combos using chickjepas, rice, oats, eggs"() {
        given:

        def input = new UserInput([
                new FoodAmountConstraint(new Chickpeas(), 70, 70),
                new FoodAmountConstraint(new Rice(), 50, 50),
                new FoodAmountConstraint(new Oats(), 50, 450),
                new FoodAmountConstraint(new Eggs(), 50, 50),
        ], 5)

        when:
        def combos = new CombinationCalculator().findCombinations(input)

        then:
        combos.every { it.totalMgByAmino()[PROTEIN] >= DailyMinimumIntake.INSTANCE.minimumMgPerAmino[PROTEIN] }
    }

    def "finds combos using buckwheat, eggs"() {
        given:

        def input = new UserInput([
                new FoodAmountConstraint(new Buckwheat(), 150, 150),
                new FoodAmountConstraint(new Eggs(), 50, 50),
                new FoodAmountConstraint(new Oats(), 50, 500),
        ], 5)

        when:
        def combos = new CombinationCalculator().findCombinations(input)

        then:
        combos.every { it.totalMgByAmino()[PROTEIN] >= 55_000 }
    }


}