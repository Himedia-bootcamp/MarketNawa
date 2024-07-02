package marketnawa.crawling.product.coupang

import com.google.gson.Gson
import marketnawa.domain.Item
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

//문자열에서 숫자 추출
fun extractPrice(text: String): String {
    val regex = Regex("[^0-9]")
    return regex.replace(text, "")
}

fun main(){
    val searchValueList: List<String> = listOf("콜라")

    val url = "https://www.coupang.com"

    val webDriverID = "webdriver.gecko.driver"
    val webDriverPath = "/Users/seung/Downloads/geckodriver"
    System.setProperty(webDriverID, webDriverPath)

    val options = FirefoxOptions()
    options.addArguments("--headless") // 브라우저를 화면에 표시하지 않고 실행 (옵션)

    val driver: WebDriver = FirefoxDriver(options)

    try{
        for(i in 1 .. searchValueList.size) {
            val searchValue = searchValueList.get(i - 1)
            println(searchValue)

            val itemList: MutableList<Item> = mutableListOf()

            for(j in 1 .. 10){
                val searchUrl = "/np/search?q=${searchValue}&listSize=72&page=$j"

                driver.get(url + searchUrl)

                val products = driver.findElements(By.cssSelector("#productList > li.search-product:not(.search-product__ad-badge)"))
//                println(products.size)

                for (product in products) {
                    val itemName = product.findElement(By.cssSelector(".name")).text.replace("\n", "")
                    val itemPriceElement = product.findElement(By.cssSelector(".price-value"))
                    val itemPrice = extractPrice(itemPriceElement.text)
                    val itemImg = product.findElement(By.cssSelector("img")).getAttribute("src")
                    val itemId = extractPrice(product.getAttribute("id"))

                    val itemLink = product.findElement(By.cssSelector("a")).getAttribute("href")
                    val item = Item(itemName, itemPrice.toInt(), itemImg, itemId, itemLink)

                    itemList.add(item)
                }
                driver.manage().deleteAllCookies()

                val gson = Gson()
                val json = gson.toJson(itemList)
//                println(json)
            }
        }
    }catch (e: Exception){
        e.printStackTrace()
    }finally {
        driver.quit()
    }

}