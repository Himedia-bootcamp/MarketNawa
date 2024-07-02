package marketnawa.crawling.product.gmarket

import com.google.gson.Gson
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.NoSuchElementException

data class Item(
    val itemName: String,
    val itemPrice: Int,
    val itemImg: String,
    val itemId: String,
    val itemLink: String
)

fun extractPrice(text: String): String {
    val regex = Regex("[^0-9]")
    return regex.replace(text, "")
}

class GmarketCrawler {

    fun execute() {
        val searchValueList: List<String> = listOf("커피믹스")

        val url = "https://www.gmarket.co.kr"

        val webDriverID = "webdriver.chrome.driver"
        val webDriverPath = "C:/drivers/chromedriver.exe" // 실제 크롬 드라이버 경로로 변경
        System.setProperty(webDriverID, webDriverPath)

        val options = ChromeOptions()
        options.addArguments("--headless")
        val driver: WebDriver = ChromeDriver(options)

        try {
            for (searchValue in searchValueList) {
                println(searchValue)

                val itemList: MutableList<Item> = mutableListOf()

                for (j in 1..10) {
                    val searchUrl = "/n/search?keyword=$searchValue&k=53&p=$j"
                    driver.get(url + searchUrl)

                    val products = try {
                        driver.findElements(By.cssSelector(".box__item-container"))
                    } catch (e: NoSuchElementException) {
                        break
                    }

                    if (products.isEmpty()) {
                        break
                    }

                    for (product in products) {
                        try {
                            val itemNameElement = product.findElement(By.cssSelector(".text__item-title"))
                            val itemPriceElement = product.findElement(By.cssSelector(".text__value"))
                            val itemImgElement = product.findElement(By.cssSelector("img"))
                            val itemLinkElement = product.findElement(By.cssSelector(".link__item"))

                            val itemName = itemNameElement?.text?.replace("\n", "") ?: "Unknown"
                            val itemPriceText = extractPrice(itemPriceElement?.text ?: "0")
                            val itemPrice = if (itemPriceText.isNotEmpty()) itemPriceText.toInt() else 0
                            val itemImg = itemImgElement?.getAttribute("src") ?: ""
                            val itemId = product.getAttribute("data-montelena-goodscode") ?: ""
                            val itemLink = itemLinkElement?.getAttribute("href") ?: ""

                            if (itemName.isNotEmpty() && itemPriceText.isNotEmpty()) {
                                val item = Item(itemName, itemPrice, itemImg, itemId, itemLink)
                                itemList.add(item)

                                println(item)
                            }
                        } catch (e: NoSuchElementException) {
                            e.printStackTrace()
                        }
                    }
                    driver.manage().deleteAllCookies()
                }
                val gson = Gson()
                val json = gson.toJson(itemList)
                println(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            driver.quit()
        }
    }
}
