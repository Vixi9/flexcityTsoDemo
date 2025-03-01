package com.example.flexcitytso.controller

import com.example.flexcitytso.dto.ActivationRequestDto
import com.example.flexcitytso.service.RecursiveActivationService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/transmissionSystemOperator")
class TsoController(private val activationService: RecursiveActivationService) {

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/activation"],
        produces = ["application/json"],
        name = "activationV1"
    )
    fun activation(@RequestParam date: LocalDate, @RequestParam volume: Int): ActivationRequestDto {
        return activationService.activate(date, volume)
    }
}