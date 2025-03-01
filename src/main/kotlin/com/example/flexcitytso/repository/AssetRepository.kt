package com.example.flexcitytso.repository

import com.example.flexcitytso.model.Asset
import org.springframework.data.jpa.repository.JpaRepository
import java.time.DayOfWeek

interface AssetRepository : JpaRepository<Asset, String> {

    fun findAssetByAvailabilitiesContaining(availabilities: List<DayOfWeek>): Collection<Asset>
}