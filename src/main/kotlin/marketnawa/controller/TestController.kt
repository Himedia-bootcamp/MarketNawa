package marketnawa.elasitcsearch

import marketnawa.domain.Category
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/test")
class TestController(private val elasticsearchOperations: ElasticsearchOperations) {


    @PostMapping("/category")
    fun save(@RequestBody category: Category): String {

        val cate: MutableList<Category> = mutableListOf()

        for(i: Int in 1..10) {
            val cated : Category = Category(
                UUID.randomUUID().toString(),
                category.lastCategory,
                category.firstCategory,
                category.secondCategory,
                category.searchText,
                category.desscription
            )
            cate.add(cated)
        }
//        val savedEntity: Category = elasticsearchOperations.save(category)
        elasticsearchOperations.save(cate)
        return "ok";
//        return savedEntity.categoryId.toString();
    }

    @GetMapping("/category/{id}")
    fun findById(@PathVariable("id") id: String): Category? {
        val category: Category? = elasticsearchOperations.get(id, Category::class.java)
        return category
    }
}