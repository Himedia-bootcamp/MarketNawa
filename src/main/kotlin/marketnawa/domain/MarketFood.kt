package marketnawa.domain

import org.joda.time.LocalDate
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "market_food")
class MarketFood(
    @Id
    @Field(name = "food_id", type = FieldType.Keyword)
    var foodId: String? = null,

    @Field(name = "food_name", type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    var foodName: String? = null,

    @Field(name = "food_price", type = FieldType.Integer)
    var foodPrice: Int? = null,

    @Field(name = "food_info_created_date", type = FieldType.Date, format = [DateFormat.date], pattern = ["yyyy.MM.dd", "yyyy.MM", "yyyy"])
    var foodInfoCreatedDate: LocalDate? = null,

    @Field(name = "food_info", type = FieldType.Keyword)
    var foodInfo: String? = null,

    @Field(name = "first_category", type = FieldType.Keyword)
    var firstCategory: String? = null,

    @Field(name = "second_category", type = FieldType.Keyword)
    var secondCategory: String? = null,

    @Field(name = "last_category", type = FieldType.Keyword)
    var lastCategory: String? = null,

    @Field(name = "detail_category", type = FieldType.Keyword)
    var detailCategory: String? = null,

    @Field(name = "food_description", type = FieldType.Keyword)
    var foodDescription: String? = null,

    @Field(name = "representative_name", type = FieldType.Keyword)
    var representativeName: String? = null,

    @Field(name = "food_marketbrand", type = FieldType.Keyword)
    var foodMarketBrand: String? = null
) {
}