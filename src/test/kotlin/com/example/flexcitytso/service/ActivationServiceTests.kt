package com.example.flexcitytso.service

import com.example.flexcitytso.exception.InsufficientAssetsException
import com.example.flexcitytso.model.Asset
import com.example.flexcitytso.repository.AssetRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.test.assertEquals

@SpringBootTest
class RecursiveActivationServiceTests(@Autowired private val activationService: ActivationService) {

    @MockitoBean
    lateinit var assetRepository: AssetRepository

    @Test
    fun activationShouldRaiseExceptionOnInsufficientAssets() {
        Mockito.`when`(
            assetRepository.findAssetByAvailabilitiesContaining(
                Mockito.anyList()
            )
        ).thenReturn(emptyList())
        assertThrows<InsufficientAssetsException> { activationService.activate(LocalDate.of(2025, 2, 24), 1) }
    }

    @Test
    fun activationShouldReturnAsset() {
        val input = listOf(Asset("asset1", "Asset 1", 2.5, mutableListOf(DayOfWeek.MONDAY), 1))
        Mockito.`when`(
            assetRepository.findAssetByAvailabilitiesContaining(
                Mockito.anyList()
            )
        ).thenReturn(
            input
        )
        assertEquals(input, activationService.activate(LocalDate.of(2025, 2, 24), 1))
    }

    @Test
    fun activationShouldReturnMinimumActivationCostAsset() {
        val input = listOf(
            Asset("asset1", "Asset 1", 2.5, mutableListOf(DayOfWeek.MONDAY), 1),
            Asset("asset2", "Asset 2", 3.0, mutableListOf(DayOfWeek.MONDAY), 1),
        )
        Mockito.`when`(
            assetRepository.findAssetByAvailabilitiesContaining(
                Mockito.anyList()
            )
        ).thenReturn(
            input
        )
        val result = activationService.activate(LocalDate.of(2025, 2, 24), 1)
        assertEquals(1, result.size)
        assertEquals("asset1", result.first().code)
    }
}