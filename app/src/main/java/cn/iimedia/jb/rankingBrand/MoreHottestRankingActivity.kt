package cn.iimedia.jb.rankingBrand

import android.os.Bundle
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.RankingListBean
import cn.iimedia.jb.http.bean.RankingListData
import cn.iimedia.jb.rankingBrand.adapter.MoreHottestAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.extension.addDivider
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.activity_more_hottest_ranking.*
import kotlinx.android.synthetic.main.load_failure.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/7.
 * 最热榜单更多页面
 */
class MoreHottestRankingActivity : BaseActivity(), OnRefreshListener, OnLoadMoreListener {
    private val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    private val els: ELS = ELS.getInstance()
    private var page = 0
    private var rankingAdapter: MoreHottestAdapter? = null
    private var dataList: ArrayList<RankingListData> = ArrayList()
    private val mActivity = this

    override fun getLayoutId(): Int = R.layout.activity_more_hottest_ranking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        refresh_layout?.setOnRefreshListener(this)
        refresh_layout?.setOnLoadMoreListener(this)
        btn_refresh?.setOnClickListener {
            refresh_layout?.autoRefresh()
        }

        ranking_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        ranking_list?.addDivider(mActivity, R.drawable.vertical_white_divider_8dp)
        if (page == 0) {
            load_failure_layout?.visibility = View.GONE
            refresh_layout?.visibility = View.VISIBLE
            refresh_layout?.autoRefresh()
        }
    }

    private fun getData() {
        if (page == 0) {
            showLoadingDialog()
        }
        val call = api.getHottestList(page, 10, els.getStringData(ELS.IMEI))
        call.enqueue(object : Callback<RankingListBean> {
            override fun onFailure(call: Call<RankingListBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "最热榜单更多页数据请求失败")
                showToast("榜单获取失败")
                load_failure_layout?.visibility = View.VISIBLE
                refresh_layout?.visibility = View.GONE
                finishState()
            }

            override fun onResponse(call: Call<RankingListBean>?, response: Response<RankingListBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    val list = bean.data
                    if (list.isNotEmpty()) {
                        dataList.addAll(list)
                        removeDuplication(dataList)
                        if (list.size < 10) {
                            refresh_layout?.finishLoadMoreWithNoMoreData()
                        } else {
                            page++
                        }
                        load_failure_layout?.visibility = View.GONE
                        refresh_layout?.visibility = View.VISIBLE
                        initAdapter()
                    } else {
                        load_failure_layout?.visibility = View.VISIBLE
                        refresh_layout?.visibility = View.GONE
                    }
                } else {
                    load_failure_layout?.visibility = View.VISIBLE
                    refresh_layout?.visibility = View.GONE
                }
                finishState()
            }
        })
    }

    private fun finishState() {
        if (refresh_layout != null && refresh_layout.isRefreshing) {
            refresh_layout.finishRefresh()
        }
        if (refresh_layout != null && refresh_layout.isLoading) {
            refresh_layout.finishLoadMore()
        }
        dismissLoadingDialog()
    }

    private fun initAdapter() {
        if (rankingAdapter == null) {
            rankingAdapter = MoreHottestAdapter(mActivity, dataList)
            ranking_list?.adapter = rankingAdapter
        } else {
            rankingAdapter?.notifyDataSetChanged()
        }
    }

    private fun removeDuplication(dataList: ArrayList<RankingListData>) {
        //最新榜单数据去重
        for (i in 0 until dataList.size - 1) {
            (dataList.size - 1 downTo i + 1)
                    .filter { dataList[it].id == dataList[i].id }
                    .forEach { dataList.remove(dataList[i]) }
        }
    }

    private fun refresh() {
        dataList.clear()
        page = 0
        refresh_layout?.setNoMoreData(false)
        getData()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout?) {
        getData()
    }
}