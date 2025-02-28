package com.example.flexcitytso.integration

import com.example.flexcitytso.model.Asset
import com.example.flexcitytso.repository.AssetRepository
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.MultiValueMap
import org.springframework.util.MultiValueMap.fromMultiValue
import java.time.DayOfWeek
import java.util.stream.Stream

private const val BASE_PATH = "/api/v1/transmissionSystemOperator"
private const val ACTIVATION_ENDPOINT = "/activation"

@SpringBootTest
@AutoConfigureMockMvc
class ActivationIntegrationTests(
    @Autowired private val assetRepository: AssetRepository, @Autowired private val mockMvc: MockMvc
) {

    companion object {
        @JvmStatic
        fun bestSolutionsOnThursday(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(1, listOf(19)),
                Arguments.of(2, listOf(19)),
                Arguments.of(3, listOf(19, 8)),
                Arguments.of(4, listOf(19, 8)),
                Arguments.of(5, listOf(12)),
                Arguments.of(6, listOf(12, 19)),
                Arguments.of(7, listOf(12, 19)),
                Arguments.of(8, listOf(12, 19, 8)),
                Arguments.of(9, listOf(12, 19, 8)),
                Arguments.of(10, listOf(12, 19, 6)),
                Arguments.of(11, listOf(12, 19, 14)),
                Arguments.of(12, listOf(12, 19, 6, 8)),
                Arguments.of(13, listOf(12, 19, 8, 14)),
                Arguments.of(14, listOf(12, 19, 14, 6)),
                Arguments.of(15, listOf(12, 19, 6, 8, 20)),
                Arguments.of(16, listOf(12, 19, 8, 14, 6)),
                Arguments.of(17, listOf(12, 19, 14, 6, 20)),
                Arguments.of(18, listOf(12, 19, 8, 14, 6, 20)),
                Arguments.of(19, listOf(12, 19, 8, 14, 6, 20)),
                Arguments.of(20, listOf(4, 6, 8, 12, 14, 19, 20)),
            )
        }
    }

    @BeforeEach
    fun clearAssets() {
        assetRepository.deleteAll()
    }

    fun createSmallDataSample() {
        val assets = listOf(
            Asset(
                "asset1", "Asset 1", 2.5, mutableListOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), 1
            ),
            Asset("asset2", "Asset 2", 5.0, mutableListOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 2),
            Asset("asset3", "Asset 3", 7.5, mutableListOf(DayOfWeek.MONDAY), 3)
        )
        assetRepository.saveAll(assets)
    }

    fun createBigDataSample() {
        val assets = listOf(
            Asset("asset1", "Asset 1", 2.5, mutableListOf(DayOfWeek.MONDAY), 1),
            Asset("asset2", "Asset 2", 5.0, mutableListOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 2),
            Asset("asset3", "Asset 3", 7.5, mutableListOf(DayOfWeek.MONDAY), 3),
            Asset(
                "asset4", "Asset 4", 12.0, mutableListOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), 1
            ),
            Asset("asset5", "Asset 5", 3.1, mutableListOf(DayOfWeek.SATURDAY), 2),
            Asset(
                "asset6", "Asset 6", 9.8, mutableListOf(
                    DayOfWeek.SUNDAY,
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY
                ), 3
            ),
            Asset("asset7", "Asset 7", 6.2, mutableListOf(), 4),
            Asset("asset8", "Asset 8", 4.7, mutableListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), 2),
            Asset("asset9", "Asset 9", 15.3, mutableListOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY), 1),
            Asset("asset10", "Asset 10", 8.1, mutableListOf(), 3),
            Asset(
                "asset11", "Asset 11", 1.9, mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), 2
            ),
            Asset("asset12", "Asset 12", 11.5, mutableListOf(DayOfWeek.THURSDAY), 5),
            Asset("asset13", "Asset 13", 5.7, mutableListOf(DayOfWeek.WEDNESDAY), 1),
            Asset(
                "asset14", "Asset 14", 13.2, mutableListOf(
                    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
                ), 4
            ),
            Asset("asset15", "Asset 15", 2.0, mutableListOf(DayOfWeek.SUNDAY), 2),
            Asset("asset16", "Asset 16", 9.1, mutableListOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY), 3),
            Asset("asset17", "Asset 17", 7.8, mutableListOf(), 1),
            Asset(
                "asset18", "Asset 18", 14.5, mutableListOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY), 5
            ),
            Asset("asset19", "Asset 19", 3.9, mutableListOf(DayOfWeek.THURSDAY, DayOfWeek.SUNDAY), 2),
            Asset(
                "asset20",
                "Asset 20",
                10.4,
                mutableListOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY),
                3
            )
        )
        assetRepository.saveAll(assets)
    }

    @Test
    @WithMockUser
    fun activationShouldReturnNotFoundOnInsufficientAssetsTest() {
        val params: MultiValueMap<String, String> =
            fromMultiValue(mapOf("date" to listOf("2025-02-24"), "volume" to listOf("1")))
        mockMvc.perform(get(BASE_PATH + ACTIVATION_ENDPOINT).params(params)).andExpect(status().isNotFound).andExpect(
            status().reason("assets.insufficient")
        )
    }

    @Test
    @WithMockUser
    fun activationShouldReturnListOf2Assets() {
        createSmallDataSample()
        val params: MultiValueMap<String, String> =
            fromMultiValue(mapOf("date" to listOf("2025-02-24"), "volume" to listOf("4")))
        mockMvc.perform(get(BASE_PATH + ACTIVATION_ENDPOINT).params(params)).andExpect(status().isOk)
            .andExpect(jsonPath("$.assets").isArray).andExpect(
                jsonPath("$.assets.size()").value(2)
            )
    }

    @Test
    @WithMockUser
    fun activationShouldReturnListOf1AssetTest1() {
        createSmallDataSample()
        val params: MultiValueMap<String, String> =
            fromMultiValue(mapOf("date" to listOf("2025-02-24"), "volume" to listOf("1")))
        mockMvc.perform(get(BASE_PATH + ACTIVATION_ENDPOINT).params(params)).andExpect(status().isOk)
            .andExpect(jsonPath("$.assets").isArray).andExpect(jsonPath("$.assets.size()").value(1))
            .andExpect(jsonPath("$.assets[0].code").value("asset1"))
    }

    @Test
    @WithMockUser
    fun activationShouldReturnListOf1AssetTest2() {
        createBigDataSample()
        val params: MultiValueMap<String, String> =
            fromMultiValue(mapOf("date" to listOf("2025-02-27"), "volume" to listOf("5")))
        mockMvc.perform(get(BASE_PATH + ACTIVATION_ENDPOINT).params(params)).andExpect(status().isOk)
            .andExpect(jsonPath("$.assets").isArray).andExpect(jsonPath("$.assets.size()").value(1))
    }

    @Test
    @WithMockUser
    fun activationShouldReturnListOf4AssetTest() {
        createBigDataSample()
        val params: MultiValueMap<String, String> =
            fromMultiValue(mapOf("date" to listOf("2025-02-26"), "volume" to listOf("10")))
        mockMvc.perform(get(BASE_PATH + ACTIVATION_ENDPOINT).params(params)).andExpect(status().isOk)
            .andExpect(jsonPath("$.assets").isArray).andExpect(jsonPath("$.assets.size()").value(4))
    }

    @MethodSource("bestSolutionsOnThursday")
    @ParameterizedTest
    @WithMockUser
    fun bestSolutionForVolumeOnThursday(volume: Int, expectedAssets: List<Int>) {
        createBigDataSample()
        val params: MultiValueMap<String, String> =
            fromMultiValue(mapOf("date" to listOf("2025-02-27"), "volume" to listOf(volume.toString())))
        mockMvc.perform(get(BASE_PATH + ACTIVATION_ENDPOINT).params(params)).andExpect(status().isOk)
            .andExpect(jsonPath("$.assets").isArray).andExpect(jsonPath("$.assets.size()").value(expectedAssets.size))
            .andExpect(
                jsonPath(
                    "$.assets[*].code",
                    containsInAnyOrder(*expectedAssets.map { "asset$it" }.toTypedArray())
                )
            )
    }
}