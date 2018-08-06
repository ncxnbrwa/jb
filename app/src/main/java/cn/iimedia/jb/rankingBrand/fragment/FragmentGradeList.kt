package cn.iimedia.jb.rankingBrand.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.RankingPropertyBean
import cn.iimedia.jb.rankingBrand.RankingBrandActivity
import cn.iimedia.jb.rankingBrand.adapter.RankingGradeAdapter
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.ScreenUtils
import kotlinx.android.synthetic.main.fragment_grade_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/4/25.
 * 综合评分
 */
class FragmentGradeList : BaseFragment() {
    private var api: APIConstants? = null
    private var els: ELS? = null
    private var rankInfoId = 0
    var gradeListener: OnGradeClickListener? = null

    companion object {
        fun getInstance(bundle: Bundle): FragmentGradeList {
            val fragment = FragmentGradeList()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_grade_list

    override fun init() {
        api = RequestEngine.createService(APIConstants::class.java)
        els = ELS.getInstance()
        val bundle: Bundle = arguments as Bundle
        rankInfoId = bundle.getInt(Config.RANK_INFO_ID, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        gradeListener = context as RankingBrandActivity
    }

    private fun getData() {
        val manager = LinearLayoutManagerWrapper(mActivity)
        manager.isSmoothScrollbarEnabled = true
        list?.layoutManager = manager
        list?.isNestedScrollingEnabled = false
        list?.setHasFixedSize(true)

        val call = api?.getRankingProPerty(35, rankInfoId, els?.getStringData(ELS.IMEI))
        call?.enqueue(object : Callback<RankingPropertyBean> {
            override fun onFailure(call: Call<RankingPropertyBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "榜单属性获取失败")
                showToast("榜单属性获取失败")
            }

            override fun onResponse(call: Call<RankingPropertyBean>?, response: Response<RankingPropertyBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    val propertiesList = bean.data.properties
                    if (propertiesList.isNotEmpty()) {
                        //百分比宽度去掉padding和position的宽度
                        list?.adapter = RankingGradeAdapter(mActivity,
                                ScreenUtils.getScreenWidth() - ScreenUtils.dp2px(70f),
                                propertiesList)
                        { position: Int ->
                            gradeListener?.gradeClickListener(position)
                        }
                    }
                }
            }
        })

    }
}