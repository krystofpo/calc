package com.polansky.amino.food

import com.polansky.amino.AminoAcid
import spock.lang.Specification

class FoodSpec extends Specification {

    def "mgPer100gOf returns exact values and matches mgPer100g map"() {
        given:
        def food = new Oats() // has concrete values

        when:
        def fromMap = food.mgPer100g
        def fromMethod = AminoAcid.values().collectEntries { aa -> [(aa): food.mgPer100gOf(aa)] }

        then:
        fromMap.size() == AminoAcid.values().length
        fromMethod.size() == AminoAcid.values().length
        AminoAcid.values().each { aa ->
            assert fromMethod[aa] == fromMap[aa]
        }
    }

    def "equals and hashcode works"(){
        given:
        def rice1 = new Rice()
        def rice2 = new Rice()

        expect:
        rice1.equals(rice2)
        rice1.hashCode() == rice2.hashCode()
    }
}
