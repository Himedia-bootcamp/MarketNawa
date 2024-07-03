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
    private val elasticsearchUtil: ElasticsearchUtil
) {

    val index: String = "market_food"

    fun searchByFoodName(indexName: String, keyword: String?, detailCategory: String?, order: String?, from: Int = 0, size: Int = 10): Any {

        /**
         *
         *  "foodMarketBrand": "G마켓",
         *  "data": [
         *          *           "foodName" : "튼튼닷컴상품명오메가3 ADE (6개월분)",
         *          *           "foodPrice" : 28800,
         *          *           "foodInfoCreatedDate" : null,
         *          *           "foodInfo" : "",
         *          *           "firstCategory" : "식품",
         *          *           "secondCategory" : "건강식품/홍삼",
         *          *           "lastCategory" : "영양제",
         *          *           "detailCategory" : "https://image.gmarket.co.kr/hanbando/202407/40d424f7-6269-490c-9f07-cc55d1292ec6.png?date=20240703",
         *          *           "foodDescription" : "",
         *          *           "representativeName" : "http://item.gmarket.co.kr/Item?goodscode=540186964",]
         */
        var dataList: List<Map<String, Any>> = emptyList()
        dataList = elasticsearchUtil.search(indexName, keyword, detailCategory, order, from, size)
        return dataList
    }
}
