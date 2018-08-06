package cn.iimedia.jb.mine.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import cn.iimedia.jb.R
import cn.iimedia.jb.mine.adapter.CollectPageAdapter
import cn.iimedia.jb.mine.fragment.FragmentCollectBrand
import cn.iimedia.jb.mine.fragment.FragmentCollectRanking
import com.xiong.appbase.base.BaseActivity
import kotlinx.android.synthetic.main.activity_collect.*

/**
 * Created by iiMedia on 2018/5/10.
 * 我的收藏页面
 */
class CollectActivity : BaseActivity() {
    val mFragments = ArrayList<Fragment>()
    private val mActivity = this

    override fun getLayoutId(): Int = R.layout.activity_collect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setNavigationOnClickListener { finish() }
        mFragments.add(FragmentCollectRanking.getInstance())
        mFragments.add(FragmentCollectBrand.getInstance())

        vp.adapter = CollectPageAdapter(supportFragmentManager, mFragments)
        //注意setup顺序,否则颜色设置会有问题
        tabs?.setDefaultNormalColor(ContextCompat.getColor(mActivity,R.color.text_color5))
        tabs?.setDefaultSelectedColor(ContextCompat.getColor(mActivity,R.color.text_color2))
        tabs?.setIndicatorWidthAdjustContent(true)
        tabs?.setupWithViewPager(vp)
        tabs?.notifyDataChanged()
    }
}