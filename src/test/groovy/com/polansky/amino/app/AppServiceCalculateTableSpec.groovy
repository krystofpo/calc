package com.polansky.amino.app

import com.polansky.amino.AminoAcid
import com.polansky.amino.CombinationCalculator
import com.polansky.amino.MinimumCalculator
import com.polansky.amino.controller.CalcFoodDto
import com.polansky.amino.controller.CalculateDto
import com.polansky.amino.food.*
import com.polansky.amino.service.AppService
import spock.lang.Specification

class AppServiceCalculateTableSpec extends Specification {

    MinimumCalculator minimumCalculator = Mock()

    def "calculateTable returns valid table with correct totals for a solvable input"() {
        given: "AppService with real foods and calculator"
        def foods = [new Chickpeas(), new Oats(), new Rice(), new Buckwheat(), new Soy(), new Peas(), new Eggs(), new Sunflower(), new Pumpkin(), new Peanut(), new Tofu()]
        def service = new AppService(foods, new CombinationCalculator(minimumCalculator))
        and: "input that certainly meets daily minima (single food, fixed grams)"
        def dto = new CalculateDto([
                new CalcFoodDto(1L, "Chickpeas", 500, 500) // 500g chickpeas easily exceeds all minima
        ])

        when:
        def list = service.calculateTable(dto)

        then: "one or more combinations returned and no error message"
        !list.isEmpty()
        list.every { it.errorMessage == null }

        and: "aminoColumns are in enum order"
        def expectedCols = AminoAcid.values()*.name() as List<String>
        list.every { it.aminoColumns == expectedCols }

        and: "totals equal the sum of the rows"
        list.each { table ->
            def sumByAmino = new LinkedHashMap<String, Double>()
            table.rows.each { row ->
                row.mgByAmino.each { k, v -> sumByAmino[k] = (sumByAmino.getOrDefault(k, 0d) + v) }
            }
            expectedCols.each { col ->
                assert Math.abs(sumByAmino.getOrDefault(col, 0d) - table.totals.totalMgByAmino.getOrDefault(col, 0d)) < 0.0001
            }
        }

        and: "RDA map contains all amino keys"
        list.each { table ->
            expectedCols.each { col -> assert table.rdaMgByAmino.containsKey(col) }
        }
    }

    def "calculateTable returns diagnostic single table with errorMessage when no combination exists"() {
        given:
        def foods = [new Rice()] // weak protein source alone
        def service = new AppService(foods, new CombinationCalculator(minimumCalculator))
        and:
        def dto = new CalculateDto([
                new CalcFoodDto(4L, "Rice", 1, 1) // impossible to meet minima
        ])

        when:
        def list = service.calculateTable(dto)

        then: "exactly one diagnostic table is returned"
        list.size() == 1
        list[0].errorMessage != null

        and: "column order matches enum"
        list[0].aminoColumns == (AminoAcid.values()*.name() as List<String>)
    }
}
