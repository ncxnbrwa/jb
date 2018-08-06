package cn.iimedia.jb.mine.fragment

import android.os.Bundle
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.CollectRankingBean
import cn.iimedia.jb.http.bean.RankInfo
import cn.iimedia.jb.mine.adapter.CollectRankingAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.extension.addDivider
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.fragment_collect_ranking.*
import kotlinx.android.synthetic.main.load_failure.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/10.
 * 收藏榜单
 */
class FragmentCollectRanking : BaseFragment(), OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    var page = 1
    var collectList: ArrayList<RankInfo> = ArrayList()
    var adapter: CollectRankingAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_collect_ranking

    companion object {
        fun getInstance(): FragmentCollectRanking {
            val f = FragmentCollectRanking()
            return f
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout?.setOnRefreshListener(this)
        refresh_layout?.setOnLoadMoreListener(this)
        btn_refresh?.setOnClickListener(this)
        ranking_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        ranking_list?.addDivider(mActivity, R.drawable.vertical_gray_divider_8dp)
        refresh_layout?.autoRefresh()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_refresh -> {
                showDataLayout()
                refresh_layout?.autoRefresh()
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        refresh()
    }

    private fun refresh() {
        page = 1
        collectList.clear()
        refresh_layout?.setNoMoreData(false)
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout?) {
        getData()
    }

    private fun getData() {
        val call = api.getCollectRankingData(1, els.getStringData(ELS.USER_ID)
                , page, 10, els.getStringData(ELS.IMEI))
        call.enqueue(object : Callback<CollectRankingBean> {
            override fun onFailure(call: Call<CollectRankingBean>?, t: Throwable?) {
                if (refresh_layout != null && refresh_layout.isRefreshing) {
                    refresh_layout?.finishRefresh()
                }
                if (refresh_layout != null && refresh_layout.isLoading) {
                    refresh_layout?.finishLoadMore()
                }
//                showFailureLayout()
                refresh_layout?.finishLoadMoreWithNoMoreData()
                DLog.w(Config.HTTP_LOG_TAG, "榜单收藏获取失败")
            }

            override fun onResponse(call: Call<CollectRankingBean>?, response: Response<CollectRankingBean>?) {
                if (refresh_layout != null && refresh_layout.isRefreshing) {
                    refresh_layout?.finishRefresh()
                }
                if (refresh_layout != null && refresh_layout.isLoading) {
                    refresh_layout?.finishLoadMore()
                }
                val bean = response?.body()
                if (bean?.code.equals("1")) {
                    val list = bean?.result
                    if (list != null && list.isNotEmpty()) {
                        showDataLayout()
                        if (list.size < 10) {
                            refresh_layout?.finishLoadMoreWithNoMoreData()
                        } else {
                            page++
                        }
                        collectList.addAll(list)
                        removeDuplication(collectList)
                        initAdapter()
                    } else {
                        if (page == 1) {
                            showEmptyLayout()
                        }
                    }
                } else {
                    showFailureLayout()
                }
            }
        })
    }

    private fun showDataLayout() {
        ll_null?.visibility = View.GONE
        load_failure_layout?.visibility = View.GONE
        refresh_layout?.visibility = View.VISIBLE
    }

    private fun showFailureLayout() {
        load_failure_layout?.visibility = View.VISIBLE
        ll_null?.visibility = View.GONE
        refresh_layout?.visibility = View.GONE
    }

    private fun showEmptyLayout() {
        load_failure_layout?.visibility = View.GONE
        ll_null?.visibility = View.VISIBLE
        refresh_layout?.visibility = View.GONE
    }

    private fun removeDuplication(dataList: ArrayList<RankInfo>) {
        //最新榜单数据去重
        for (i in 0 until dataList.size - 1) {
            (dataList.size - 1 downTo i + 1)
                    .filter { dataList[it].id == dataList[i].id }
                    .forEach { dataList.remove(dataList[i]) }
        }
    }

    private fun initAdapter() {
        if (adapter == null) {
            adapter = CollectRankingAdapter(mActivity, collectList)
            ranking_list?.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
        }
    }
}