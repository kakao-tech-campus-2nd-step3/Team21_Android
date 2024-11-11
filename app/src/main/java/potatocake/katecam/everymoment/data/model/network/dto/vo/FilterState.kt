package potatocake.katecam.everymoment.data.model.network.dto.vo

data class FilterState(
    val selectedEmotions: String?,
    val isBookmarked: Boolean,
    val startDate: String?,
    val endDate: String?,
    val selectedCategories: String?
)
