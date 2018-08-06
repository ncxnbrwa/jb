package cn.iimedia.jb.classify

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.classify.adapter.HotBrandAdapter
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.BrandListBean
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.extension.addGridDivider
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.fragment_hot_brand.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/25.
 * 热门品牌页面
 */
class FragmentHotBrand : BaseFragment() {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    var layoutManager: GridLayoutManager? = null
    var adapter: HotBrandAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_hot_brand

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (layoutManager == null) {
            layoutManager = GridLayoutManager(mActivity, 3)
            hot_brand_list?.layoutManager = layoutManager
            hot_brand_list?.addGridDivider(mActivity, 16)
        }
    }

    fun getHotBrand() {
        baseActivity?.showLoadingDialog()
        val getHotBrandCall = api.getHotBrandList(els.getStringData(ELS.IMEI))
        getHotBrandCall?.enqueue(object : Callback<BrandListBean> {
            override fun onFailure(call: Call<BrandListBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "热门品牌请求失败")
                baseActivity?.dismissLoadingDialog()
            }

            override fun onResponse(call: Call<BrandListBean>?, response: Response<BrandListBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    val dataList = bean.data
                    if (dataList.isNotEmpty()) {
                        if (adapter == null) {
                            adapter = HotBrandAdapter(mActivity, dataList)
                            hot_brand_list?.adapter = adapter
                        } else {
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
                baseActivity?.dismissLoadingDialog()
            }
        })
    }
}