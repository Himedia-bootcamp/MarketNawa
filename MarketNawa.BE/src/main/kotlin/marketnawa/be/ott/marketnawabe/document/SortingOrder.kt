package marketnawa.be.ott.marketnawabe.document

import java.util.*

enum class SortingOrder {
    HIGHEST,
    LOWEST,
    DEFAULT;

    companion object {
        // 문자열을 SortingOrder Enum으로 변환하는 메서드
        fun fromString(value: String?): SortingOrder {
            return when (value?.uppercase(Locale.getDefault())) {
                "HIGHEST" -> HIGHEST
                "LOWEST" -> LOWEST
                null -> DEFAULT  // 기본 값 설정
                else -> throw IllegalArgumentException("Unsupported sorting order: $value")
            }
        }
    }
}