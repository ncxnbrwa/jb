package cn.iimedia.jb.rankingBrand.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by iiMedia on 2018/4/25.
 * 榜单页ViewPager Adapter
 */
class RankingPagerAdapter(fm: FragmentManager, val list: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
    private val mTitles = arrayListOf<String>("综合评分","言值","企业实力","媒体热度","用户口碑")

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles.get(position)
    }
}