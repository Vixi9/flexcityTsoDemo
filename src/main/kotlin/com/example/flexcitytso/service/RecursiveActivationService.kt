package com.example.flexcitytso.service

import com.example.flexcitytso.dto.ActivationRequestDto
import com.example.flexcitytso.dto.toDto
import com.example.flexcitytso.exception.InsufficientAssetsException
import com.example.flexcitytso.model.Asset
import com.example.flexcitytso.repository.AssetRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service("recursiveActivationService")
class RecursiveActivationService(private val assetRepository: AssetRepository) : ActivationService {

    override fun activate(date: LocalDate, volume: Int): ActivationRequestDto {
        val dayFilteredAssets = assetRepository.findAssetByAvailabilitiesContaining(listOf(date.dayOfWeek))
        val solution = tryAndFindSolution(dayFilteredAssets, volume)
        if (solution.isEmpty()) throw InsufficientAssetsException() else return solution.toDto()
    }

    /**
     * Find the best solution for the given volume
     * This might be terrible in terms of performance since we don't store or memoize sub solutions
     * @param assets the assets to search in
     * @param volume the desired volume of assets to activate
     */
    fun tryAndFindSolution(assets: Collection<Asset>, volume: Int): Collection<Asset> {

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
            val firstHalf = tryAndFindSolution(assets, volume - i)
            val secondHalf = tryAndFindSolution(assets - firstHalf.toSet(), i)
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