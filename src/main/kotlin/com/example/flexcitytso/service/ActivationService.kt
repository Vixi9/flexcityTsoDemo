package com.example.flexcitytso.service

import com.example.flexcitytso.dto.ActivationRequestDto
import com.example.flexcitytso.dto.toDto
import com.example.flexcitytso.exception.InsufficientAssetsException
import com.example.flexcitytso.model.Asset
import com.example.flexcitytso.repository.AssetRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate

@Service("recursiveActivationService")
class ActivationService(private val assetRepository: AssetRepository) {

    fun activate(date: LocalDate, volume: Int): ActivationRequestDto {
        val dayOfWeek: ArrayList<DayOfWeek> = arrayListOf(date.dayOfWeek)
        val dayFilteredAssets = assetRepository.findAssetByAvailabilitiesContaining(dayOfWeek)
        val solution = findSolution(dayFilteredAssets, volume)
        if (solution.isEmpty()) throw InsufficientAssetsException() else return solution.toDto()
    }

    /**
     * Find the best solution for the given volume
     * This might be terrible in terms of performance
     */
    fun findSolution(assets: Collection<Asset>, volume: Int): Collection<Asset> {

        // Try and find a solution that exactly matches the exact volume or the closest one
        // This is an interpretation of the problem since we don't know if an asset can be activated partially or not
        var solution = try {
            listOf(assets.filter { asset: Asset -> asset.volume >= volume }
                .minBy { asset: Asset -> asset.activationCost })
        } catch (e: NoSuchElementException) {
            emptyList()
        }

        // Try sub solutions expecting a better one
        for (i in 1..volume / 2) {
            val firstHalf = findSolution(assets, volume - i)
            val secondHalf = findSolution(assets - firstHalf.toSet(), i)
            if (firstHalf.isNotEmpty() && secondHalf.isNotEmpty()) {
                val newSolution = firstHalf + secondHalf
                if (solution.isEmpty() || newSolution.sumOf { asset: Asset -> asset.activationCost } < solution.sumOf { asset: Asset -> asset.activationCost }) {
                    solution = newSolution
                }
            }
        }

        return solution
    }
}