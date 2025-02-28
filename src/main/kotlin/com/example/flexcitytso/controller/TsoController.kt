package com.example.flexictytso.controller

import com.example.flexictytso.model.Asset
import com.example.flexictytso.service.ActivationService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
@RequestMapping("api/v1/transmissionSystemOperator")
class TsoController(private val activationService: ActivationService) {

    @GetMapping("/activation")
    fun activation(@RequestParam date: LocalDate, @RequestParam volume: Int): Iterable<Asset> {
        return activationService.activate(date, volume)
    }
}