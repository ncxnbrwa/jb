package cn.iimedia.jb.homepage

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.homepage.adapter.SearchBrandAdapter
import cn.iimedia.jb.homepage.adapter.SearchPreviewAdapter
import cn.iimedia.jb.homepage.adapter.SearchRankingAdapter
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.RankInfo
import cn.iimedia.jb.http.bean.RankingBrandsList
import cn.iimedia.jb.http.bean.SearchBean
import cn.iimedia.jb.http.bean.SearchDatabase
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.extension.setTextStyle
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.KeyboardUtils
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.more_button.*
import kotlinx.android.synthetic.main.search_hot_label.*
import org.litepal.crud.DataSupport
import org.litepal.crud.callback.FindMultiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by iiMedia on 2018/5/18.
 * 搜索页
 */
class SearchActivity : BaseActivity(), View.OnClickListener {
    private val TAG = "SearchActivity"
    private val mActivity = this
    private val labelList = arrayListOf<String>("婴儿", "波轮洗衣机", "眼霜", "除螨仪"
            , "面膜", "口红", "巧克力", "电子烟", "无人机", "奶粉")
    private val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    private val els: ELS = ELS.getInstance()
    private var pageNum = 1
    private var search: SearchView? = null
    private var previewAdapter: SearchPreviewAdapter? = null
    private var rankingAdapter: SearchRankingAdapter? = null
    private var brandAdapter: SearchBrandAdapter? = null
    private val searchHistoryList = ArrayList<SearchDatabase>()
    private val resultRankingList = ArrayList<RankInfo>()
    private val resultBrandList = ArrayList<RankingBrandsList>()
    private var finalQuery = ""
    private var resultCount = 0

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cancel?.setOnClickListener(this)
        search = findViewById(R.id.search)
        search?.setTextStyle(12f, R.color.text_color5, R.color.text_color3)
        search?.isFocusable = true
        search?.isIconified = false //设置searchView处于展开状态
        search?.requestFocusFromTouch()
        search?.onActionViewExpanded() // 当展开无输入内容的时候，没有关闭的图标
        search?.queryHint = "输入搜索关键词" //设置默认无内容时的文字提示

        //去掉下划线
        val c = search?.javaClass
        try {
            val f = c?.getDeclaredField("mSearchPlate")//通过反射，获得类对象的一个属性对象
            f?.isAccessible = true
            val v = f?.get(search) as View//获得属性的值
            v.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color2))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        initContent()
        addLabel()
        getPreview()

        search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    querySubmit(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isEmpty()) {
                    getPreview()
                }
                return false
            }
        })
    }

    private fun initContent() {
        refresh_layout?.isEnableRefresh = false
        refresh_layout?.setOnLoadMoreListener { querySubmit(finalQuery, false) }
        detail?.text = "加载更多"
        preview_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        preview_list?.isNestedScrollingEnabled = false
        search_ranking_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        search_ranking_list?.isNestedScrollingEnabled = false
        search_brand_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        search_brand_list?.isNestedScrollingEnabled = false

    }

    private fun addLabel() {
        hot_label_layout?.visibility = View.VISIBLE
        for (i in labelList) {
            float_layout?.addView(MyUtils.createRelatedItem(mActivity, i) { v: View? ->
                search?.setQuery(i, false)
                querySubmit(i)
            })
        }
    }

    private fun getPreview() {
        searchHistoryList.clear()
        preview_layout?.visibility = View.VISIBLE
        refresh_layout?.visibility = View.GONE
        pageNum = 1
        resultCount = 0
        refresh_layout?.setNoMoreData(false)
        //从数据库获取搜索历史
        DataSupport.order("id desc").limit(10)
                .findAsync(SearchDatabase::class.java).listen(object : FindMultiCallback {
            override fun <T : Any?> onFinish(t: MutableList<T>?) {
                searchHistoryList.addAll(t as ArrayList<SearchDatabase>)
                DLog.w(TAG, "搜索历史数据:$searchHistoryList")
                if (previewAdapter == null) {
                    previewAdapter = SearchPreviewAdapter(mActivity, searchHistoryList,
                            { data: SearchDatabase, position: Int, v: View ->
                                when (v.id) {
                                    R.id.delete -> {
                                        DataSupport.delete(SearchDatabase::class.java, data.id)
                                        if (position > 0) {
                                            //因为有个头布局,所以-1
                                            searchHistoryList.removeAt(position - 1)
                                            previewAdapter?.notifyItemRemoved(position)
                                        }
                                    }
                                    else -> {
                                        //第二个参数false只修改搜索文字,true还会提交搜索,走监听设置的方法
                                        search?.setQuery(data.searchName, false)
                                        querySubmit(data.searchName)
                                    }
                                }
                            },
                            {
                                for (data in searchHistoryList) {
                                    data.delete()
                                }
                                searchHistoryList.clear()
                                previewAdapter?.notifyDataSetChanged()
                            })
                    preview_list?.adapter = previewAdapter
                } else {
                    previewAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    //搜索提交
    private fun querySubmit(query: String, save: Boolean = true) {
        if (pageNum == 1) {
            resultBrandList.clear()
            resultRankingList.clear()
            showLoadingDialog()
            preview_layout?.visibility = View.GONE
            refresh_layout?.visibility = View.VISIBLE
            result_hint?.visibility = View.GONE
            result_list_layout?.visibility = View.GONE
        }
        finalQuery = query
        //保存搜索记录
        if (save) {
            SearchDatabase.saveNotRepeat(query)
        }
        val searchCall = api.querySearch(query, els.getStringData(ELS.USER_ID)
                , 0, pageNum, 10, els.getStringData(ELS.IMEI))
        searchCall.enqueue(object : Callback<SearchBean> {
            override fun onResponse(call: Call<SearchBean>?, response: Response<SearchBean>?) {
                val bean = response?.body()
                when (bean?.code) {
                    "1" -> {
                        if (bean.brandlist.isNotEmpty()) {
                            val brandTmpList = bean.brandlist
                            resultBrandList.addAll(brandTmpList)
                            resultCount += brandTmpList.size
                        }
                        if (bean.rankInfoList.isNotEmpty()) {
                            val rankTmpList = bean.rankInfoList
                            resultRankingList.addAll(rankTmpList)
                            resultCount += rankTmpList.size
                        }
                        //是否显示列表
                        if (resultCount == 0) {
                            result_list_layout?.visibility = View.GONE
                            showResultCount(0)
                        } else {
                            removeDuplication(resultBrandList, resultRankingList)
                            //显示搜索结果条目
                            showResultCount(resultCount)
                            initResultAdapter(query)
                            result_list_layout?.visibility = View.VISIBLE
                            if (bean.brandlist.size > 5) {
                                more?.visibility = View.VISIBLE
                            } else {
                                more?.visibility = View.GONE
                            }
                            if (bean.rankInfoList.size < 10) {
                                refresh_layout?.finishLoadMoreWithNoMoreData()
                            } else {
                                pageNum++
                            }
                        }
                    }
                    "-1" -> {
                        showToast(resources.getString(R.string.server_error))
                        showResultCount(0)
                    }
                    "3" -> {
                        showToast(resources.getString(R.string.parameter_error))
                        showResultCount(0)
                    }
                }
                dismissLoadingDialog()
                finishLoad()
            }

            override fun onFailure(call: Call<SearchBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "搜索失败")
                dismissLoadingDialog()
                finishLoad()
                showResultCount(0)
            }
        })
    }

    private fun initResultAdapter(query: String) {
        if (brandAdapter == null) {
            brandAdapter = SearchBrandAdapter(mActivity, resultBrandList, query)
            search_brand_list?.adapter = brandAdapter
        } else {
            brandAdapter?.key = query
            brandAdapter?.notifyDataSetChanged()
        }
        if (rankingAdapter == null) {
            rankingAdapter = SearchRankingAdapter(mActivity, resultRankingList, query)
            search_ranking_list?.adapter = rankingAdapter
        } else {
            rankingAdapter?.key = query
            rankingAdapter?.notifyDataSetChanged()
        }
    }

    private fun finishLoad() {
        if (refresh_layout != null && refresh_layout.isLoading) {
            refresh_layout?.finishLoadMore()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //点击空白处收起键盘
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (KeyboardUtils.isShouldHideKeyboard(view, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                try {
                    imm.hideSoftInputFromWindow(view.windowToken, 0);
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //以防万一去重
    private fun removeDuplication(brandList: ArrayList<RankingBrandsList>
                                  , rankingList: ArrayList<RankInfo>) {
        for (i in 0 until brandList.size - 1) {
            (brandList.size - 1 downTo i + 1)
                    .filter { brandList[i] == brandList[it] }
                    .forEach { brandList.remove(brandList[i]) }
        }
        for (i in 0 until rankingList.size - 1) {
            (rankingList.size - 1 downTo i + 1)
                    .filter { rankingList[i] == rankingList[it] }
                    .forEach { rankingList.remove(rankingList[i]) }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cancel -> finish()
        }
    }

    private fun showResultCount(resultCount: Int) {
        result_hint?.text = "此次搜索共为你找出${resultCount}条结果"
        result_hint?.visibility = View.VISIBLE
    }
}