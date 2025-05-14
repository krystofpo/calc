package com.polansky.amino

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AminoApplication

fun main(args: Array<String>) {
	runApplication<AminoApplication>(*args)
}
