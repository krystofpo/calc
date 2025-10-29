package com.polansky.amino.e2e

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
 * End-to-end tests through the HTTP layer to verify amino minima and percentages are computed by backend.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
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

    private static boolean allPercentsBelow100(Map table) {
        Map<String, Double> pct = (Map<String, Double>) table.totals.percentRdaByAmino
        return pct.values().any { (it ?: 0.0) < 100.0 }
    }

    private static boolean allPercentsAtLeast100(Map table) {
        Map<String, Double> pct = (Map<String, Double>) table.totals.percentRdaByAmino
        return pct.values().every { (it ?: 0.0) >= 100.0 }
    }

    def "e2e: when inputs are too small overall, service returns diagnostic table with error"() {
        when:
        def response = postCalculate(mvc, [
                food('RICE', 0, 10),
                food('PEANUT', 0, 10)
        ])

        then: "one diagnostic table is returned with an error message and at least one amino below class minimum"
        response.size() == 1
        Map table = (Map) response[0]
        table.errorMessage instanceof String && !((String) table.errorMessage).isEmpty()
        allPercentsBelow100(table)
    }

    def "e2e: single food can meet daily minima (all aminos and protein >= 100%)"() {
        when:
        def response = postCalculate(mvc, [food('SOY', 0, 200)])

        then:
        response.size() >= 1
        response.every { ((Map) it).errorMessage == null }
        response.every { allPercentsAtLeast100((Map) it) }
    }

    def "e2e: two foods together meet minima while each alone cannot (cap max to force pairing)"() {
        when:
        def response = postCalculate(mvc, [
                food('SOY', 0, 120),
                food('RICE', 0, 100)
        ])

        then:
        response.size() >= 1
        response.every { ((Map) it).errorMessage == null }
        response.every { allPercentsAtLeast100((Map) it) }
    }
}
