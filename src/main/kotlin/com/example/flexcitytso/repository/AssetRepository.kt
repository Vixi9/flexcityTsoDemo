package com.example.flexcitytso.repository

import com.example.flexcitytso.model.Asset
import org.springframework.data.repository.CrudRepository
import java.time.DayOfWeek

interface AssetRepository : CrudRepository<Asset, String> {

    fun findAssetByAvailabilitiesContaining(availabilities: List<DayOfWeek>): Collection<Asset>
}