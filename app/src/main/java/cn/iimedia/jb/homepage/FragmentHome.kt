package cn.iimedia.jb.homepage

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.CommonUtils
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.common.intentHottestMore
import cn.iimedia.jb.common.intentToSearch
import cn.iimedia.jb.homepage.adapter.FeatureBrandAdapter
import cn.iimedia.jb.homepage.adapter.HottestListAdapter
import cn.iimedia.jb.homepage.adapter.LatestListAdapter
import cn.iimedia.jb.homepage.adapter.TopicListAdapter
import cn.iimedia.jb.homepage.holder.BannerViewHolder
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.*
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.extension.addDivider
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_banner_item.*
import kotlinx.android.synthetic.main.home_top.*
import kotlinx.android.synthetic.main.load_failure.*
import kotlinx.android.synthetic.main.more_button.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by iiMedia on 2018/4/23.
 * 主页
 */
class FragmentHome : BaseFragment(), OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    var api: APIConstants? = null
    val horizontal = LinearLayoutManager.HORIZONTAL
    val els: ELS = ELS.getInstance()
    val latestList: ArrayList<RankingListData> = ArrayList()
    private var latestAdapter: LatestListAdapter? = null
    var latestPage = 0
    var statusBarFlag = false
    private val STATUS_FLAG = "status_flag"

    override fun getLayoutId(): Int = R.layout.fragment_home

    companion object {
        fun getInstance(): FragmentHome {
            val fragment = FragmentHome()
            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATUS_FLAG, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = RequestEngine.createService(APIConstants::class.java)
        refresh_layout.setOnRefreshListener(this)
        refresh_layout.setOnLoadMoreListener(this)
        more?.setOnClickListener(this)
        btn_refresh?.setOnClickListener(this)
        rl_search?.setOnClickListener(this)

        hottest_list?.layoutManager = LinearLayoutManagerWrapper(mActivity, horizontal, false)
        hottest_list?.addDivider(mActivity, R.drawable.grid_white_divider, horizontal)
        featured_list?.layoutManager = LinearLayoutManagerWrapper(mActivity, horizontal, false)
        featured_list?.addDivider(mActivity, R.drawable.grid_white_divider, horizontal)

        topic_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        topic_list?.isNestedScrollingEnabled = false
        topic_list?.addDivider(mActivity, R.drawable.vertical_white_divider_16dp)
        latest_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        latest_list?.isNestedScrollingEnabled = false
        latest_list?.addDivider(mActivity, R.drawable.vertical_white_divider_8dp)
        refresh_layout?.autoRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        refresh()
    }

    //暴露给MainActivity刷新的方法
    fun tabRefresh() {
        data_layout?.scrollTo(0, 0)
        refresh_layout?.autoRefresh()
    }

    fun refresh() {
        latestList.clear()
        refresh_layout?.setNoMoreData(false)
        latestPage = 0
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout?) {
        getLatestData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.more -> intentHottestMore(mActivity)
            R.id.btn_refresh -> {
                refresh_layout?.visibility = View.VISIBLE
                load_failure_layout?.visibility = View.GONE
                refresh_layout?.autoRefresh()
            }
            R.id.rl_search -> intentToSearch(mActivity)
        }
    }

    fun getData() {
        getBannerData()
    }

    private fun getBannerData() {
        val getBannerCall = api?.getBanner(0, els.getStringData(ELS.IMEI))
        getBannerCall?.enqueue(object : Callback<BannerBean> {
            override fun onResponse(call: Call<BannerBean>?, response: Response<BannerBean>?) {
                val bean = response?.body()
                if (bean?.code.equals("1")) {
                    val list = bean?.result
                    if (list != null && list.isNotEmpty()) {
                        refresh_layout?.visibility = View.VISIBLE
                        load_failure_layout?.visibility = View.GONE
                        configBanner(list)
                        getHottestData()
                    } else {
                        refresh_layout?.visibility = View.GONE
                        load_failure_layout?.visibility = View.VISIBLE
                    }
                } else {
                    refresh_layout?.visibility = View.GONE
                    load_failure_layout?.visibility = View.VISIBLE
                }
                finishRefresh()
            }

            override fun onFailure(call: Call<BannerBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "轮播数据请求失败")
                refresh_layout?.visibility = View.GONE
                load_failure_layout?.visibility = View.VISIBLE
                finishRefresh()
            }
        })
    }

    //配置轮播
    fun configBanner(imgList: ArrayList<BannerResult>) {
        //数据少于3条,轮播没有魅族效果
//        imgList.add(imgList[imgList.size - 1])
//        imgList.add(imgList[imgList.size - 1])
        CommonUtils.setBanner(vp_banner, imgList, { BannerViewHolder() })
        indicator_view?.totalPage = imgList.size
        vp_banner?.addPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                indicator_view?.currentPage = position
            }
        })
        indicator_view?.invalidate()
        vp_banner?.setIndicatorVisible(false)
        vp_banner?.start()
    }

    fun getHottestData() {
        //请求最热榜单数据
        val getHottestCall = api?.getHottestList(0, 5, els.getStringData(ELS.IMEI))
        getHottestCall?.enqueue(object : Callback<RankingListBean> {
            override fun onFailure(call: Call<RankingListBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "获取最热榜单列表失败")
                home_hottest_root?.visibility = View.GONE
                finishRefresh()
                getFeaturedData()
            }

            override fun onResponse(call: Call<RankingListBean>?, response: Response<RankingListBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    val dataList = bean.data
                    if (dataList.isNotEmpty()) {
                        //有数据时才显示整个模块
                        home_hottest_root?.visibility = View.VISIBLE
                        hottest_list?.adapter = HottestListAdapter(mActivity, dataList)
                    } else {
                        home_hottest_root?.visibility = View.GONE
                    }
                } else {
                    home_hottest_root?.visibility = View.GONE
                }
                finishRefresh()
                getFeaturedData()
            }
        })
    }

    private fun getFeaturedData() {
        //获取小众精选/热门品牌数据
//        val getFeaturedCall = api?.getFeatruedList(els.getStringData(ELS.IMEI))
        val getHotBrandCall = api?.getHotBrandList(els.getStringData(ELS.IMEI))
        getHotBrandCall?.enqueue(object : Callback<BrandListBean> {
            override fun onFailure(call: Call<BrandListBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "热门品牌请求失败")
                home_featured_root?.visibility = View.GONE
                getTopicData()
                finishRefresh()
            }

            override fun onResponse(call: Call<BrandListBean>?, response: Response<BrandListBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    val dataList = bean.data
                    if (dataList.isNotEmpty()) {
                        //有数据时才显示整个模块
                        home_featured_root?.visibility = View.VISIBLE
                        featured_list?.adapter = FeatureBrandAdapter(mActivity, dataList)
                    } else {
                        home_featured_root?.visibility = View.GONE
                    }
                } else {
                    home_featured_root?.visibility = View.GONE
                }
                finishRefresh()
                getTopicData()
            }
        })
    }

    private fun getTopicData() {
        //获取专题列表数据
        val getTopicCall = api?.getTopicList(els.getStringData(ELS.IMEI), els.getStringData(ELS.IMEI))
        getTopicCall?.enqueue(object : Callback<TopicBean> {
            override fun onFailure(call: Call<TopicBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "专题获取失败")
                topic_list?.visibility = View.GONE
                finishRefresh()
                getLatestData()
            }

            override fun onResponse(call: Call<TopicBean>?, response: Response<TopicBean>?) {
                val bean = response?.body()
                if (bean?.code.equals("1")) {
                    val topicList = bean?.specialList
                    if (mActivity != null && topicList != null && topicList.isNotEmpty()) {
                        topic_list?.visibility = View.VISIBLE
                        topic_list?.adapter = TopicListAdapter(mActivity, topicList)
                    } else {
                        topic_list?.visibility = View.GONE
                    }
                } else {
                    topic_list?.visibility = View.GONE
                }
                finishRefresh()
                getLatestData()
            }
        })
    }

    private fun getLatestData() {
        //获取最新榜单数据
        val getLatestListCall = api?.getLatestList(latestPage, 10, els.getStringData(ELS.IMEI))
        getLatestListCall?.enqueue(object : Callback<RankingListBean> {
            override fun onResponse(call: Call<RankingListBean>?, response: Response<RankingListBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    val dataList = bean.data
                    if (dataList.isNotEmpty()) {
                        //有数据时才显示整个模块
                        home_latest_root?.visibility = View.VISIBLE
                        latestList.addAll(dataList)
                        removeDuplication(latestList)
                        initLatestAdapter()
                        if (dataList.size < 10) {
                            refresh_layout?.finishLoadMoreWithNoMoreData()
                        } else {
                            latestPage++
                        }
                    } else {
                        home_latest_root?.visibility = View.GONE
                    }
                }
                finishLoad()
            }

            override fun onFailure(call: Call<RankingListBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "最新榜单获取失败")
                home_latest_root?.visibility = View.GONE
                finishLoad()
            }
        })
    }

    private fun initLatestAdapter() {
        if (mActivity == null) return
        if (latestAdapter == null) {
            latestAdapter = LatestListAdapter(mActivity, latestList)
            latest_list?.adapter = latestAdapter
        } else {
            latestAdapter?.notifyDataSetChanged()
        }
    }

    private fun finishRefresh() {
        if (refresh_layout != null && refresh_layout.isRefreshing) {
            refresh_layout.finishRefresh()
        }
    }

    private fun finishLoad() {
        if (refresh_layout != null && refresh_layout.isLoading) {
            refresh_layout?.finishLoadMore()
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

    override fun onPause() {
        super.onPause()
        vp_banner?.pause()
    }

    override fun onResume() {
        super.onResume()
        vp_banner?.start()
    }

}