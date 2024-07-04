package marketnawa.be.ott.marketnawabe.common

import org.springframework.http.HttpStatus

data class ResponseDTO(
    var status: Int = 0,     // 상태코드값(200,404,415, 400, 500)
    var message: String = "", // 응답메세지
    var data: Any? = null    // 응답데이터
) {
    constructor() : this(0, "", null)

    constructor(status: HttpStatus, message: String) : this(status.value(), message, null)

    constructor(status: HttpStatus, message: String, data: Any?) : this(status.value(), message, data)

    constructor(message: String) : this(0, message, null)
}