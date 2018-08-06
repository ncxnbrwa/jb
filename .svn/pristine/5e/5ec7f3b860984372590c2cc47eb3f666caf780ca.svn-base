package cn.iimedia.jb.classify

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.classify.adapter.TwoLevelAdapter
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.ClassifyLevelBean
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.fragment_two_level.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/8.
 * 榜单大全二级分类页面
 */
class FragmentTwoLevel : BaseFragment() {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    var layoutManager: LinearLayoutManagerWrapper? = null
    val resuleList: ArrayList<ArrayList<Any?>> = ArrayList()
    var adapter: TwoLevelAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_two_level

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (layoutManager == null) {
            layoutManager = LinearLayoutManagerWrapper(mActivity)
        }
        two_level_list.layoutManager = layoutManager
        val pool = RecyclerView.RecycledViewPool()
        pool.setMaxRecycledViews(0, 10)
        two_level_list.recycledViewPool = pool
        if (adapter == null) {
            adapter = TwoLevelAdapter(mActivity, resuleList)
        }
        two_level_list.adapter = adapter
    }

    fun getTwoLevelData() {
        baseActivity?.showLoadingDialog()
        resuleList.clear()
        val bundle = arguments
        val levelId: Int = bundle!!.getInt(Config.TWO_LEVEL_ID, 0)

        val twoLevelCall = api.getTwoLevel(levelId, els.getStringData(ELS.IMEI))
        twoLevelCall.enqueue(object : Callback<ClassifyLevelBean> {
            override fun onResponse(call: Call<ClassifyLevelBean>?, response: Response<ClassifyLevelBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    //不管有误数据必须刷新adapter,否则会有空数据复用之前的数据问题
                    if (bean.resuleList != null && bean.resuleList.isNotEmpty()) {
                        resuleList.addAll(bean.resuleList)
                        Handler(Looper.getMainLooper()).post({
                            adapter?.notifyDataSetChanged()
                        })
                    } else {
                        Handler(Looper.getMainLooper()).post({
                            adapter?.notifyDataSetChanged()
                        })
                    }
                }
                baseActivity?.dismissLoadingDialog()
            }

            override fun onFailure(call: Call<ClassifyLevelBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "二级分类数据请求失败")
                baseActivity?.dismissLoadingDialog()
            }
        })
    }
}