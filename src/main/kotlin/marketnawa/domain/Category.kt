package marketnawa.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field


@Document(indexName = "category")
class Category(
    @Id
    @Field(name = "category_id")
    var categoryId: String?,
    @Field("first_category")
    var firstCategory: String?,
    @Field("second_category")
    var secondCategory: String?,
    @Field("last_category")
    var lastCategory: String?,
    @Field("desscription")
    var desscription: String?,
    @Field(name = "search_text")
    var searchText: String?
) {
}