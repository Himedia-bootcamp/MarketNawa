package marketnawa.be.ott.marketnawabe.service

import marketnawa.be.ott.marketnawabe.document.MarketFood
import marketnawa.be.ott.marketnawabe.document.SortingOrder
import marketnawa.be.ott.marketnawabe.dto.MarketFoodDTO
import marketnawa.be.ott.marketnawabe.util.ElasticsearchUtil
import org.modelmapper.ModelMapper
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val elasticsearchUtil: ElasticsearchUtil,
    private val modelMapper: ModelMapper
) {

    val index: String = "market_food"

    fun searchByFoodName(indexName: String, keyword: String?, order: String?, from: Int = 0, size: Int = 10): Map<String, MutableList<MarketFoodDTO>> {

        var ssgDTOList: MutableList<MarketFoodDTO> = mutableListOf()
        var coupangDTOList: MutableList<MarketFoodDTO> = mutableListOf()
        var gMarketDTOList: MutableList<MarketFoodDTO> = mutableListOf()

        fun searchAndAddToDTOList(market: String, dtoList: MutableList<MarketFoodDTO>) {
            try {
                val searchList = elasticsearchUtil.search(indexName, keyword, market, order, from, size)
                searchList.forEach { hits ->
                    val source = hits["_source"] as? Map<String, Any>
                    val marketFood = MarketFoodDTO(
                        source?.get("foodId")?.toString() ?: "",
                        source?.get("foodRealId")?.toString() ?: "",
                        source?.get("foodName")?.toString() ?: "",
                        source?.get("foodPrice")?.toString()?.toIntOrNull() ?: 0,
                        null,
                        source?.get("foodInfo")?.toString() ?: "",
                        source?.get("foodLink")?.toString() ?: "",
                        source?.get("firstCategory")?.toString() ?: "",
                        source?.get("secondCategory")?.toString() ?: "",
                        source?.get("lastCategory")?.toString() ?: "",
                        source?.get("detailCategory")?.toString() ?: "",
                        source?.get("foodDescription")?.toString() ?: "",
                        source?.get("representativeName")?.toString() ?: "",
                        source?.get("foodMarketBrand")?.toString() ?: "",
                        source?.get("foodImg")?.toString() ?: ""
                    )
                    dtoList.add(marketFood)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        searchAndAddToDTOList("SSG", ssgDTOList)
        searchAndAddToDTOList("Gmarket", gMarketDTOList)
        searchAndAddToDTOList("coupang", coupangDTOList)

        return mapOf(
            "SSG" to ssgDTOList,
            "Gmarket" to gMarketDTOList,
            "coupang" to coupangDTOList
        )
    }
}
