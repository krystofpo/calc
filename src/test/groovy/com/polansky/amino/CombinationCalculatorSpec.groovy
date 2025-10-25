package com.polansky.amino

import com.polansky.amino.food.Buckwheat
import com.polansky.amino.food.Chickpeas
import com.polansky.amino.food.Eggs
import com.polansky.amino.food.Oats
import com.polansky.amino.food.Rice
import com.polansky.amino.service.TotalsCalculator
import spock.lang.Specification
import spock.lang.Subject

import static com.polansky.amino.AminoAcid.PROTEIN

class CombinationCalculatorSpec extends Specification {

    MinimumCalculator minimumCalculator = Mock()
    TotalsCalculator totalsCalculator = new TotalsCalculator()

    @Subject
    CombinationCalculator combinationCalculator


    def setup(){
       combinationCalculator = new CombinationCalculator(minimumCalculator, totalsCalculator)
    }

    def "throws if maxGrams from user input can’t meet daily minima"() {
        given:
        minimumCalculator.isMoreThanMinimum(_) >> false
        def food = new Rice()
        def input = new UserInput([
                new FoodAmountConstraint(food, 1, 1)
        ])

        when:
       combinationCalculator.findCombinations(input)

        then:
        thrown(LittleAminoException)
    }

    def "when user input meets daily minimums, returns a list of combinations and each combo meets daily minimums"() {
        given:
        minimumCalculator.isMoreThanMinimum(_) >>> [true, false, false, true, true] //first true is the early check, rest is odometer checks
        def rice = new Rice()
        def input = new UserInput([
                new FoodAmountConstraint(rice, 10, 25)
        ])

        when:
        def combos = combinationCalculator.findCombinations(input) // odometer: 10 false, 15 false, 20 true, 25 true

        then:
        combos.size() == 2
        combos.any{ it.gramsByFood[rice] == 20 && it.gramsByFood.size() ==1 }
        combos.any{ it.gramsByFood[rice] == 25 && it.gramsByFood.size() ==1 }
    }


    def "when user input meets daily minimums, returns a list of combinations and each combo meets daily minimums - two foods"() {
        given:
        def rice = new Rice()
        def oats = new Oats()
        def input = new UserInput([
                new FoodAmountConstraint(rice, 10, 15),
                new FoodAmountConstraint(oats, 10, 15)
        ])

        minimumCalculator.isMoreThanMinimum(_) >>> [true, false, true, true, true ] //first true is the early check, rest is odometer checks [10,10] , [15,10], [10,15], [15,15]

        when:
        def results = combinationCalculator.findCombinations(input)

        then:
        results.size() == 3
        results.any{ it.gramsByFood[rice] == 15 && it.gramsByFood[oats] == 10 && it.gramsByFood.size() ==2 }
        results.any{it.gramsByFood[rice] == 10 && it.gramsByFood[oats] == 15 && it.gramsByFood.size() ==2 }
        results.any{it.gramsByFood[rice] == 15 && it.gramsByFood[oats] == 15 && it.gramsByFood.size() ==2 }
    }

}