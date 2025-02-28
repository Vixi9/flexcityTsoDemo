package com.example.flexcitytso.dto

import com.example.flexcitytso.model.Asset

data class ActivationRequestDto(
    val totalActivationCost: Double,
    val totalVolume: Int,
    val assets: List<AssetDto>
)

data class AssetDto(
    val code: String,
    val name: String,
    val activationCost: Double,
    val volume: Int,
)

fun Collection<Asset>.toDto(): ActivationRequestDto {
    return ActivationRequestDto(
        totalActivationCost = sumOf { it.activationCost },
        totalVolume = sumOf { it.volume },
        assets = map { it.toDto() }
    )
}

fun Asset.toDto(): AssetDto {
    return AssetDto(code, name, activationCost, volume)
}