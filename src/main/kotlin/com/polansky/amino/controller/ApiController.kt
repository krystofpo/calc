package com.polansky.amino.controller


import com.polansky.amino.service.AppService
import com.polansky.amino.dto.CombinationTableDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


data class FoodDto(val id: String, val name: String)

@RestController
@RequestMapping("/api")
class ApiController(val appService: AppService) {

    @GetMapping("foods")
    fun suggest(@RequestParam suggest: String): List<FoodDto> { //TODO url params
        log.info { "/api/foods?suggest=$suggest" }

        return appService.suggest(suggest)
    }

    @PostMapping("calculate")
    fun calculate(@RequestBody calculateDto: CalculateDto): List<CombinationTableDto> {
        log.info { "$calculateDto" }
        return appService.calculateTable(calculateDto)
    }
}

data class CalculateDto(val foods: List<CalcFoodDto>)

data class CalcFoodDto(val id: String,val name: String, val min: Int, val max: Int)

private val log = KotlinLogging.logger {}