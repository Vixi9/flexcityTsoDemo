package com.example.flexcitytso.service

import com.example.flexcitytso.exception.InsufficientAssetsException
import com.example.flexcitytso.model.Asset
import com.example.flexcitytso.repository.AssetRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.logging.Logger

@Service("recursiveActivationService")
class RecursiveActivationService(private val assetRepository: AssetRepository) : ActivationService {
    companion object {
        val LOG: Logger = Logger.getLogger(RecursiveActivationService::class.java.name)
    }

    override fun activate(date: LocalDate, volume: Int): Collection<Asset> {
        val dayOfWeek: ArrayList<DayOfWeek> = arrayListOf(date.dayOfWeek)
        val dayFilteredAssets = assetRepository.findAssetByAvailabilitiesContaining(dayOfWeek)
        return filter(dayFilteredAssets, volume)

    }

    fun filter(assets: Collection<Asset>, volume: Int): Collection<Asset> {
        if (assets.isEmpty()) {
            throw InsufficientAssetsException()
        }

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
            val firstHalf = filter(assets, i)
            val secondHalf = filter(assets - firstHalf.toSet(), volume - i)
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