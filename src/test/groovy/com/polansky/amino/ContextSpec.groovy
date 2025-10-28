package com.polansky.amino

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification


@SpringBootTest
class ContextSpec extends Specification{

    @Autowired
    CombinationCalculator combinationCalculator

    def "context check"(){
expect:
        combinationCalculator != null
    }
}
