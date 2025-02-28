package com.example.flexcitytso.service

import com.example.flexcitytso.model.Asset
import com.example.flexcitytso.repository.AssetRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate

@Service("greedyActivationService")
class GreedyActivationService(private val assetRepository: AssetRepository) {

    fun activate(date: LocalDate, volume: Int): Collection<Asset> {
        val dayOfWeek: ArrayList<DayOfWeek> = arrayListOf(date.dayOfWeek)
        val dayFilteredAssets = assetRepository.findAssetByAvailabilitiesContaining(dayOfWeek)
        return filter(dayFilteredAssets, volume)

    }

    private fun filter(dayFilteredAssets: Collection<Asset>, volume: Int): Collection<Asset> {
        TODO("Not yet implemented")
    }
}