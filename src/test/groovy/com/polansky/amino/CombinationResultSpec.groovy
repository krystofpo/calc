package com.polansky.amino

import com.polansky.amino.food.Food
import spock.lang.Specification

import static com.polansky.amino.AminoAcid.*

class CombinationResultSpec extends Specification {

    static class TestFood extends Food {
        long id; String name; String link=""
        @Override long getId(){return id}
        @Override String getName(){return name}
        @Override String getLink(){return link}
        @Override int getHistidineMgPer100g(){return 100}
        @Override int getIsoleucineMgPer100g(){return 200}
        @Override int getLeucineMgPer100g(){return 300}
        @Override int getLysineMgPer100g(){return 400}
        @Override int getMethionineMgPer100g(){return 500}
        @Override int getPhenylalanineMgPer100g(){return 600}
        @Override int getThreonineMgPer100g(){return 700}
        @Override int getTryptophanMgPer100g(){return 800}
        @Override int getValineMgPer100g(){return 900}
        @Override int getProteinMgPer100g(){return 1000}
    }

    def "totalMgByAmino sums per food correctly"() {
        given:
        def f1 = new TestFood(id:1, name:"F1")
        def f2 = new TestFood(id:2, name:"F2")
        // grams: 100g of f1 => values as-is; 50g of f2 => half
        def cr = new CombinationResult([(f1):100, (f2):50])

        when:
        def totals = cr.totalMgByAmino()

        then:
        totals[HISTIDINE] == 100 + 50
        totals[ISOLEUCINE] == 200 + 100
        totals[LEUCINE] == 300 + 150
        totals[LYSINE] == 400 + 200
        totals[METHIONINE] == 500 + 250
        totals[PHENYLALANINE] == 600 + 300
        totals[THREONINE] == 700 + 350
        totals[TRYPTOPHAN] == 800 + 400
        totals[VALINE] == 900 + 450
        totals[PROTEIN] == 1000 + 500
    }
}
