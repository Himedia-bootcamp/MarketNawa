package marketnawa.crawling.product.ssg

import marketnawa.domain.Category
import marketnawa.domain.MarketFood
import marketnawa.elasitcsearch.EsSearchService
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

class SsgCrawler(
    val category: Category,
//    private val elasticsearchOperations: ElasticsearchOperations
    private val esSearchService: EsSearchService
) {
    fun execute() {
        //크롤링할 웹사이트 주소
        val url = "https://www.ssg.com"

        //웹 드라이버 설정
        //현재 파이어폭스로 설정되어 있음, 크롬사용시 변경필요
        val webDriverID = "webdriver.chrome.driver"
        val webDriverPath = "C:/drivers/chromedriver.exe" // 실제 크롬 드라이버 경로로 변경
        System.setProperty(webDriverID, webDriverPath)

        val options = FirefoxOptions()
        options.addArguments("--headless") // 브라우저를 화면에 표시하지 않고 실행 (옵션)
        val driver: WebDriver = FirefoxDriver(options)

        try {
            //키워드 설정
            val searchText = category.searchText
            println(searchText.toString())

            for (j in 1..10) {
                val searchUrl = "/search.ssg?target=all&query=$searchText&page=$j"
                driver.get(url + searchUrl)

                //ul 안에 있는 class가 cunit_t232 인 li 요소 찾음
                // 페이지가 존재하는지 확인
                val products = try {
                    driver.findElements(By.cssSelector("#idProductImg > li.cunit_t232"))
                } catch (e: NoSuchElementException) {
                    break
                }

                // 페이지에 제품이 없으면 루프 종료
                if (products.isEmpty()) {
                    break
                }

                //찾은 요소에서 필요한 제품 정보 저장
                for (product in products) {
                    val itemName = product.findElement(By.cssSelector(".title")).text.replace("\n", "")
                    val itemPriceElement = product.findElement(By.cssSelector(".ssg_price"))
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
                        foodMarketBrand = "SSG"
                    )
                    if (marketFood != null) {
//                        elasticsearchOperations.save(marketFood)
                        esSearchService.indexDocument("market_food", marketFood)
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


