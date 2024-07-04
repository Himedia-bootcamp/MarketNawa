package marketnawa.be.ott.marketnawabe.common
import kotlin.math.ceil

data class PageDTO(
    private val cri: Criteria, // 검색 정보
    private val total: Int // 행 전체 개수
) {
    var pageStart: Int = 0 // 페이지 시작 번호
        private set
    var pageEnd: Int = 0 // 페이지 끝 번호
        private set
    var next: Boolean = false // 다음 버튼 존재 여부
        private set
    var prev: Boolean = false // 이전 버튼 존재 여부
        private set

    init {
        /* 페이지 끝 번호 */
        pageEnd = (ceil(cri.pageNum / 10.0)).toInt() * 10

        /* 페이지 시작 번호 */
        pageStart = pageEnd - 9

        /* 전체 마지막 페이지 번호 */
        val realEnd = (ceil(total * 1.0 / cri.amount)).toInt()

        /* 페이지 끝 번호 유효성 체크 */
        if (realEnd < pageEnd) {
            pageEnd = realEnd
        }

        /* 이전 버튼 값 초기화 */
        prev = pageStart > 1

        /* 다음 버튼 값 초기화 */
        next = pageEnd < realEnd
    }
}
