package com.example.flexcitytso.service

import com.example.flexcitytso.dto.ActivationRequestDto
import java.time.LocalDate

fun interface ActivationService {

    fun activate(date: LocalDate, volume: Int): ActivationRequestDto
}