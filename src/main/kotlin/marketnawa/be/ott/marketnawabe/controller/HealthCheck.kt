package marketnawa.be.ott.marketnawabe.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @RequestMapping("/health")
    fun healthCheck() = "OK!"
}
