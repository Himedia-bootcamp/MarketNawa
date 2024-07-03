package marketnawa.be.ott.marketnawabe.service

import marketnawa.be.ott.marketnawabe.common.Criteria
import marketnawa.be.ott.marketnawabe.common.PageDTO
import marketnawa.be.ott.marketnawabe.common.PagingResponseDTO
import marketnawa.be.ott.marketnawabe.document.MarketFood
import marketnawa.be.ott.marketnawabe.dto.MarketFoodDTO
import marketnawa.be.ott.marketnawabe.repository.FoodRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val foodRepository: FoodRepository,
    private val modelMapper: ModelMapper
) {
    fun searchByFoodName(keyword: String, cri: Criteria, sort: String, detail: String): Map<String, PagingResponseDTO> {

        val index: Int = cri.pageNum - 1
        val count: Int = cri.amount
        val sorting: Sort =
            when (sort) {
                "낮은가격" -> Sort.by("food_price").ascending()
                "높은가격" -> Sort.by("food_price").descending()
                else -> Sort.by("_score").descending()
            }

        val paging: Pageable = PageRequest.of(index, count, sorting)

        val brands = listOf("Coupang", "G마켓", "SSG")

        val brandResults = brands.associateWith { brand ->
            try {
                val foodList: Page<MarketFood> = foodRepository.findByFoodNameContaining(keyword, detail, brand, paging)

                val dtoList: List<MarketFoodDTO> = foodList.content.map { food ->
                    modelMapper.map(food, MarketFoodDTO::class.java)
                }
                PagingResponseDTO(PageImpl(dtoList, paging, foodList.totalElements), PageDTO(cri, dtoList.size))
            } catch (e: Exception) {
                val emptyPage: Page<MarketFoodDTO> = PageImpl(listOf(), paging, 0)
                PagingResponseDTO(emptyPage, PageDTO(cri, 0))
            }
        }
        return brandResults
    }
}
