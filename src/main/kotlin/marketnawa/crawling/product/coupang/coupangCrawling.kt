package marketnawa.crawling.product.coupang

import marketnawa.domain.Category
import marketnawa.domain.MarketFood
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import java.util.*
import kotlin.NoSuchElementException

//문자열에서 숫자 추출
fun extractPrice(text: String): String {
    val regex = Regex("[^0-9]")
    return regex.replace(text, "")
}

class CoupCrawler(
    val category: Category,
    private val elasticsearchOperations: ElasticsearchOperations
) {
    fun execute() {

        val url = "https://www.coupang.com"

        val webDriverID = "webdriver.gecko.driver"
        val webDriverPath = "/Users/seung/Downloads/geckodriver"
        System.setProperty(webDriverID, webDriverPath)

        val options = FirefoxOptions()
        options.addArguments("--headless") // 브라우저를 화면에 표시하지 않고 실행 (옵션)

        val driver: WebDriver = FirefoxDriver(options)

        try {
            val searchText = category.searchText

            for (j in 1..10) {
                val searchUrl = "/np/search?q=${searchText}&listSize=72&page=$j"

                driver.get(url + searchUrl)

                val products = try {
                    driver.findElements(By.cssSelector("#productList > li.search-product:not(.search-product__ad-badge)"))
                } catch (e: NoSuchElementException) {
                    break
                }

                // 페이지에 제품이 없으면 루프 종료
                if (products.isEmpty()) {
                    break
                }

                for (product in products) {
                    val itemName = product.findElement(By.cssSelector(".name")).text.replace("\n", "")
                    val itemPriceElement = product.findElement(By.cssSelector(".price-value"))
                    val itemPrice = extractPrice(itemPriceElement.text)
                    val itemImg = product.findElement(By.cssSelector("img")).getAttribute("src")
                    val itemId = extractPrice(product.getAttribute("id"))

                    val itemLink = product.findElement(By.cssSelector("a")).getAttribute("href")

                    val marketFood: MarketFood? = MarketFood(
                        foodId = UUID.randomUUID().toString(),
                        foodRealId = itemId,
                        foodName = itemName,
                        foodPrice = itemPrice.toInt(),
//                        foodInfoCreatedDate = "",
                        foodInfo = "",
                        foodLink = itemLink,
                        foodImg = itemImg,
                        firstCategory = category.firstCategory,
                        secondCategory = category.secondCategory,
                        lastCategory = category.lastCategory,
                        representativeName = category.desscription,
                        foodMarketBrand = "coupang"
                    )

                    //gmarket, ssg

                    if (marketFood != null) {
                        elasticsearchOperations.save(marketFood)
                    }
                }
                driver.manage().deleteAllCookies()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            driver.quit()
        }
    }
}

