package com.polansky.amino.e2e

import com.polansky.amino.DailyMinimumIntake
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

/**
 * End-to-end tests through the HTTP layer to verify amino minimums calculation.
 *
 * We purposefully test the system at the controller boundary (ApiController)
 * to cover wiring, AppService, CombinationCalculator, formatters, and domain.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// full context, no web server
@AutoConfigureMockMvc
class ApiCalculateE2ESpec extends Specification {

    @Autowired
    MockMvc mvc


    private static Map<String, Object> food(String id, int min, int max) {
        [id: id, name: id, min: min, max: max]
    }

    private static List<Map> postCalculate(MockMvc mvc, List<Map> foods) {
        def payload = [foods: foods]
        String json = JsonOutput.toJson(payload)
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post('/api/calculate')
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn()

        assert result.response.status == 200: "Unexpected status ${result.response.status}: ${result.response.contentAsString}"
        return (List<Map>) new JsonSlurper().parseText(result.response.contentAsString)
    }

    /**
     * Build a map of class-defined daily minima keyed by amino name (String),
     * matching the JSON response keys.
     */
    private static Map<String, Double> classMinima() {
        // Kotlin object is exposed as DailyMinimumIntake.INSTANCE
        def m = DailyMinimumIntake.INSTANCE.minimumMgPerAmino
        // Convert enum keys to their name() to match JSON keys
        m.collectEntries { k, v -> [(k.name()): (v as Double)] }
    }

    /**
     * Verify totals from the response table meet or exceed class minima.
     */
    private static boolean tableTotalsMeetClassMinima(Map table) {
        Map<String, Double> totals = (Map<String, Double>) table.totals.totalMgByAmino
        Map<String, Double> minByAmino = classMinima()
        return minByAmino.keySet().every { k ->
            def total = (totals[k] ?: 0.0) as double
            def min = (minByAmino[k] ?: 0.0) as double
            return total + 1e-6 >= min
        }
    }

    /**
     * Verify RDA values embedded in the response == class minima.
     */
    private static boolean responseRdasEqualClassMinima(Map response) {
        Map<String, Double> responseMinima = (Map<String, Double>) response.rdaMgByAmino
        Map<String, Double> classMinima = classMinima()
        return classMinima.every { amino, classMin ->
            def responseMin = (responseMinima[amino] ?: 0.0) as double
            return responseMin == classMin
        }
    }

    def "e2e: when inputs are too small overall, service returns diagnostic table with error"() {
        when: "we cap max grams so even maximums cannot meet daily minima"
        def response = postCalculate(mvc, [
                food('RICE', 0, 10),
                food('PEANUT', 0, 10)
        ])

        then: "one diagnostic table is returned with an error message and at least one amino below class minimum"
        response.size() == 1
        Map table = (Map) response[0]
        table.errorMessage instanceof String && !((String) table.errorMessage).isEmpty()
        // verify there exists at least one amino that is below its CLASS minimum
        Map<String, Double> responseTotals = (Map<String, Double>) table.totals.totalMgByAmino
        Map<String, Double> minByAmino = classMinima()
        minByAmino.any { amino, classMin -> (responseTotals[amino] ?: 0.0) < classMin }
        responseRdasEqualClassMinima(table)
    }

    def "e2e: single food can meet daily minima (all aminos and protein >= 100%)"() {
        when: "we allow enough of a strong food (SOY) to meet all thresholds"
        def response = postCalculate(mvc, [food('SOY', 0, 200)])

        then:
        response.size() >= 1
        response.every { ((Map) it).errorMessage == null }
        response.every { tableTotalsMeetClassMinima((Map) it) }
        response.every { responseRdasEqualClassMinima((Map) it) }
    }

    def "e2e: two foods together meet minima while each alone cannot (cap max to force pairing)"() {
        when: "cap SOY below protein RDA and let RICE complement to reach it"
        // SOY at 120g gives ~51.6g protein; add RICE up to 100g gives ~6g more protein -> meet 57g
        def response = postCalculate(mvc, [
                food('SOY', 0, 120),
                food('RICE', 0, 100)
        ])

        then:
        response.size() >= 1
        response.every { ((Map) it).errorMessage == null }
        response.every { tableTotalsMeetClassMinima((Map) it) }
        response.every { responseRdasEqualClassMinima((Map) it) }
    }
}
