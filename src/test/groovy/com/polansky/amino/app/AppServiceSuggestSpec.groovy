package com.polansky.amino.app

import com.polansky.amino.CombinationCalculator
import com.polansky.amino.MinimumCalculator
import com.polansky.amino.food.*
import com.polansky.amino.service.AppService
import com.polansky.amino.service.ResultTableFormatter
import spock.lang.Specification

class AppServiceSuggestSpec extends Specification {
    CombinationCalculator combinationCalculator = Mock()
    ResultTableFormatter resultTableFormatter = Mock()

    def "suggest filters case-insensitively and returns id+name only"() {
        given:
        def foods = [new Oats(), new Rice(), new Buckwheat(), new Soy(), new Peas()]
        def service = new AppService(foods, combinationCalculator, resultTableFormatter)

        when:
        def result = service.suggest(query)

        then:
        result*.name.every { it.toLowerCase().contains(query.toLowerCase()) }
        result.every { it.id != null && it.name }

        where:
        query << ["o", "RI", "pea", "buck"]
    }
}
