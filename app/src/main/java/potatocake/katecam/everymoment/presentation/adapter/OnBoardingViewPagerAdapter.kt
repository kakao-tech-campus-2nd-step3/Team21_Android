package potatocake.katecam.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.vo.AppGuide
import potatocake.katecam.everymoment.databinding.ItemOnboardingBinding

class OnBoardingViewPagerAdapter :
    RecyclerView.Adapter<OnBoardingViewPagerAdapter.PagerViewHolder>() {

    private val pages = listOf(
        AppGuide(
            "자동 기록",
            "한 장소에서 사용자가 설정한 시간이 지나면\n일기가 자동기록됩니다",
            R.drawable.guide_today_log,
        ),
        AppGuide(
            "앱 설정",
            "정확한 자동기록을 위해 절전모드는 항상 OFF\n 위치는 항상 허용해주세요",
            R.drawable.guide_setting
        ),
        AppGuide(
            "피드 기능",
            "친구와 일기를 공유해보세요",
            R.drawable.guide_feed
        ),
        AppGuide(
            "캘린더",
            "캘린더에서 날짜를 누르면 그 날짜에 쓴\n 일기 화면으로 이동합니다",
            R.drawable.guide_calendar
        ),
        AppGuide(
            "일기 검색",
            "자신의 일기를 다양한 필터로 검색해보세요",
            R.drawable.guide_search
        ),
        AppGuide(
            "설정",
            "프로필,닉네임,알림 설정 등\n 자신에게 맞는 설정을 해보세요",
            R.drawable.guide_profile
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size

    class PagerViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: AppGuide) {
            binding.appGuideTitle.text = page.title
            binding.appGuideDescription.text = page.description
            binding.appGuideImage.setImageResource(page.imageResId)
        }
    }
}