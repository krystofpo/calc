package com.polansky.amino

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class AminoApplicationSpec extends Specification {


	def "context ok"() {
		expect:
		1==1
	}

}
