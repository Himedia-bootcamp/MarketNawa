package marketnawa.be.ott.marketnawabe.dto

import org.joda.time.LocalDate
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

class MarketFoodDTO(
    var foodId: String? = null,

    var foodRealId: String? = null,

    var foodName: String? = null,

    var foodPrice: Int? = null,

    var foodInfoCreatedDate: LocalDate? = null,

    var foodInfo: String? = null,

    var foodLink: String? = null,

    var firstCategory: String? = null,

    var secondCategory: String? = null,

    var lastCategory: String? = null,

    var detailCategory: String? = null,

    var foodDescription: String? = null,

    var representativeName: String? = null,

    var foodMarketBrand: String? = null,

    var foodImg: String? = null
)
