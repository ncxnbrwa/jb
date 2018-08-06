package cn.iimedia.jb.classify

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.ClassifyLevelBean
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.ScreenUtils
import kotlinx.android.synthetic.main.fragment_classify.*
import kotlinx.android.synthetic.main.load_failure.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/8.
 * 分类页面
 */
class FragmentClassify : BaseFragment(), View.OnClickListener {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    //存储左侧边栏View
    val oneLevelTextList: ArrayList<TextView> = ArrayList()
    //记录一级菜单已选位置
    var lastCheck = 0
    var fragmentTwoLevel: FragmentTwoLevel? = null
    var fragmentHotBrand: FragmentHotBrand? = null
    var currentFragment: Fragment? = null

    override fun getLayoutId(): Int = R.layout.fragment_classify

    companion object {
        fun getInstance(): FragmentClassify {
            val fragment = FragmentClassify()
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_refresh?.setOnClickListener(this)
        //添加热门品牌到左侧
        val textView = createText("推荐品牌")
        oneLevelTextList.add(textView)
        ll_one_level.addView(textView)
        oneLevelTextList[0].setTextCheck()
        //初始化右侧fragment
        fragmentHotBrand = FragmentHotBrand()
        fragmentTwoLevel = FragmentTwoLevel()
        loadMultipleRootFragment(R.id.two_level_content, 0, fragmentHotBrand, fragmentTwoLevel)
        initData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_refresh -> {
                load_failure_layout?.visibility = View.GONE
                initData()
            }
        }
    }

    private fun initData() {
        fragmentHotBrand?.getHotBrand()
        getOneLevelData()
    }

    private fun getOneLevelData() {
        val oneLevelCall = api.getOneLevel(els.getStringData(ELS.IMEI))
        oneLevelCall.enqueue(object : Callback<ClassifyLevelBean> {
            override fun onFailure(call: Call<ClassifyLevelBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "一级分类数据请求失败")
                load_failure_layout?.visibility = View.VISIBLE
                data_layout?.visibility = View.GONE
            }

            override fun onResponse(call: Call<ClassifyLevelBean>?, response: Response<ClassifyLevelBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    load_failure_layout?.visibility = View.GONE
                    data_layout?.visibility = View.VISIBLE
                    val dataList = bean.resuleList
                    if (dataList.isNotEmpty()) {
                        for (data in dataList) {
                            val textView = createText(data[1].toString())
                            oneLevelTextList.add(textView)
                            ll_one_level.addView(textView)
                        }
                        //设置左侧边栏点击事件
                        for (i in oneLevelTextList.indices) {
                            oneLevelTextList[i].setOnClickListener { _: View? ->
                                //点击不同的item才切换加载
                                if (i != lastCheck) {
                                    oneLevelTextList[i].setTextCheck()
                                    oneLevelTextList[lastCheck].setTextCheck(false)
                                    lastCheck = i
                                    if (i == 0) {
                                        showHideFragment(fragmentHotBrand, fragmentTwoLevel)
                                        fragmentHotBrand?.getHotBrand()
                                    } else {
                                        showHideFragment(fragmentTwoLevel, fragmentHotBrand)
                                        val index = (dataList[i - 1][0] as Number).toInt()
                                        notifyLevelTwo(index)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    load_failure_layout?.visibility = View.VISIBLE
                    data_layout?.visibility = View.GONE
                }
            }
        })
    }

    //代码新建左侧边栏TextView
    private fun createText(text: String): TextView {
        val textView = TextView(mActivity)
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , ScreenUtils.getScreenHeight() / 13)
        textView.text = text
        textView.setBackgroundResource(R.drawable.classify_left_bg_normal)
        textView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color5))
        textView.textSize = 13f
        textView.gravity = Gravity.CENTER
        return textView
    }

    //手动更改左侧边栏文字check状态
    fun TextView.setTextCheck(check: Boolean = true) {
        if (check) {
            setBackgroundResource(R.drawable.classify_left_bg_sel)
            setTextColor(ContextCompat.getColor(mActivity, R.color.text_color2))
        } else {
            setTextColor(ContextCompat.getColor(mActivity, R.color.text_color5))
            setBackgroundResource(R.drawable.classify_left_bg_normal)
        }
    }

    //调用右侧fragment的网络请求二级分类
    private fun notifyLevelTwo(twoLevelId: Int) {
        val bundle = Bundle()
        bundle.putInt(Config.TWO_LEVEL_ID, twoLevelId)
        fragmentTwoLevel?.arguments = bundle
        fragmentTwoLevel?.getTwoLevelData()
    }

    private fun add(fragment: Fragment, tag: String) {
        val fragmentManager = baseActivity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()
        var fragment2 = fragment
        //优先检查，fragment是否存在，避免重叠
        val tempFragment = fragmentManager?.findFragmentByTag(tag)
        if (tempFragment != null) {
            fragment2 = tempFragment
        }
        if (fragment2.isAdded) {
            addOrShowFragment(transaction!!, fragment2, tag)
        } else {
            //如果现在有fragment就先隐藏现有的再添加,没有则直接添加
            if (currentFragment != null && currentFragment!!.isAdded) {
                transaction?.hide(currentFragment)?.add(R.id.two_level_content, fragment2, tag)?.commitAllowingStateLoss()
            } else {
                transaction?.add(R.id.two_level_content, fragment2, tag)?.commitAllowingStateLoss()
            }
            currentFragment = fragment
        }
    }

    //添加或者显示fragment
    private fun addOrShowFragment(transaction: FragmentTransaction, fragment: Fragment, tag: String) {
        if (currentFragment == fragment)
            return
        if (!fragment.isAdded) {
            // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.two_level_content, fragment, tag).commitAllowingStateLoss()
        } else {
            transaction.hide(currentFragment).show(fragment).commitAllowingStateLoss()
        }
        currentFragment?.userVisibleHint = false
        currentFragment = fragment
        currentFragment?.userVisibleHint = true
    }

}
