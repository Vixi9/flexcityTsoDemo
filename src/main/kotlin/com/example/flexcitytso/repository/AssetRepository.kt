package com.example.flexictytso.repository

import com.example.flexictytso.model.Asset
import org.springframework.data.repository.Repository
import java.time.DayOfWeek

interface AssetRepository : Repository<Asset, String> {
    fun findAssetsByAvailabilitiesContainingAndVolumeGreaterThanEqual(
        availabilities: List<DayOfWeek>, volume: Int
    ): Iterable<Asset>
}