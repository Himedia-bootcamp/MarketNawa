package marketnawa.crawling.category.danawa

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.time.Duration
import java.util.concurrent.TimeUnit
import org.elasticsearch.client.RestHighLevelClient
import marketnawa.util.IndexingUtil

data class Category(
    val categoryLarge: String,
    val categoryMedium: String,
    val categorySmall: String,
    val description: String,
    val searchText: String
)

fun getFoodCategoryDetails(
    restHighLevelClient: RestHighLevelClient,
    indexingUtil: IndexingUtil
): List<Category> {
    val options = ChromeOptions()
    options.addArguments("--headless")
    options.addArguments("--disable-gpu")
    options.addArguments("--no-sandbox")

    val driver: WebDriver = ChromeDriver(options)
    val url = "https://www.danawa.com/"

    val foodCategories = mutableListOf<Category>()
    val processedCategories = mutableSetOf<String>()

    try {
        driver.get(url)
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

        val foodCategoryElement: WebElement = driver.findElement(By.cssSelector("li.category__list__row[group-code='16']"))

        val actions = Actions(driver)
        actions.moveToElement(foodCategoryElement).perform()

        val foodSubCategoryElement: WebElement = WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.category__depth__row[category-code='2973']"))
        )

        actions.moveToElement(foodSubCategoryElement).perform()

        val document: Document = Jsoup.parse(driver.pageSource)

        val foodCategoryElementJsoup = document.select("li.category__list__row[group-code=16]").first()

        if (foodCategoryElementJsoup != null) {
            var foodMainCategory = foodCategoryElementJsoup.select("a.category__list__btn").text()
            if (foodMainCategory == "식품·유아·완구") {
                foodMainCategory = "식품"
            }

            val subCategoryElements: Elements = foodCategoryElementJsoup.select("div#categoryHoverLayer16 ul.category__depth > li")

            if (subCategoryElements.isEmpty()) {
                println("No subcategories found.")
            }

            val excludeCategoryCodes = listOf(2971, 30960) + (27211..27212) + (38150..38159) + (49000..49999) + (52000..53999)

            for (element in subCategoryElements) {
                val categoryCode = element.attr("category-code").toIntOrNull() ?: continue
                if (categoryCode in excludeCategoryCodes) continue

                val mainSubCategory = element.select("strong.category__depth__title").text()
                val detailCategories = element.select("a.category__depth__link, a.category__depth__btn")

                for (detail in detailCategories) {
                    val dataCateList = detail.attr("data-catelist")
                    val dataCateListParts = dataCateList.split("_")

                    if (dataCateListParts.size > 2) {
                        val middleCategory = dataCateListParts[1]
                        var subCategoryText = dataCateListParts[2]
                        subCategoryText = subCategoryText.replace("인기메뉴", "").replace("신규메뉴", "").trim()

                        val categoryKey = "$foodMainCategory|$middleCategory|$subCategoryText"
                        if (categoryKey !in processedCategories) {
                            processedCategories.add(categoryKey)

                            val subDetailElements = detail.nextElementSibling()?.select("ul.category__depth > li")
                            if (subDetailElements != null && subDetailElements.isNotEmpty()) {
                                for (subDetail in subDetailElements) {
                                    val subDetailText = subDetail.select("span.category__depth__txt").text()
                                    val cleanedSubDetailText = subDetailText.replace("인기메뉴", "").replace("신규메뉴", "").trim()
                                    val description = cleanedSubDetailText
                                    val searchText = if (description.isNotEmpty()) description else if (subCategoryText.isNotEmpty()) subCategoryText else middleCategory
                                    val cleanedSearchText = searchText.replace("인기메뉴", "").replace("신규메뉴", "").trim()
                                    val category = Category(
                                        categoryLarge = foodMainCategory,
                                        categoryMedium = middleCategory,
                                        categorySmall = subCategoryText,
                                        description = description,
                                        searchText = cleanedSearchText
                                    )
                                    indexingUtil.indexInsert(
                                        "market_nawa",
                                        category.hashCode().toString(),
                                        category
                                    )
                                    foodCategories.add(category)
                                }
                            } else {
                                val description = ""
                                val searchText = if (description.isNotEmpty()) description else if (subCategoryText.isNotEmpty()) subCategoryText else middleCategory
                                val cleanedSearchText = searchText.replace("인기메뉴", "").replace("신규메뉴", "").trim()
                                val category = Category(
                                    categoryLarge = foodMainCategory,
                                    categoryMedium = middleCategory,
                                    categorySmall = subCategoryText,
                                    description = description,
                                    searchText = cleanedSearchText
                                )

                                indexingUtil.indexInsert(
                                    "market_nawa",
                                    category.hashCode().toString(),
                                    category
                                )
                                foodCategories.add(category)
                            }
                        }
                    } else {
//                        println("data-catelist does not contain expected parts: $dataCateList")
                    }
                }
            }
        } else {
            println("식품/유아/완구 카테고리를 찾을 수 없습니다.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        driver.quit()
    }

    return foodCategories
}
