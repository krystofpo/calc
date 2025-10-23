package com.polansky.amino.app

import com.polansky.amino.CombinationCalculator
import com.polansky.amino.controller.CalcFoodDto
import com.polansky.amino.controller.CalculateDto
import com.polansky.amino.food.*
import spock.lang.Specification

class AppServiceSuggestSpec extends Specification {

    def "suggest filters case-insensitively and returns id+name only"() {
        given:
        def foods = [new Oats(), new Rice(), new Buckwheat(), new Soy(), new Peas()]
        def service = new AppService(foods, new CombinationCalculator())

        when:
        def result = service.suggest(query)

        then:
        result*.name.every { it.toLowerCase().contains(query.toLowerCase()) }
        result.every { it.id != null && it.name }

        where:
        query << ["o", "RI", "pea", "buck"]
    }
}
