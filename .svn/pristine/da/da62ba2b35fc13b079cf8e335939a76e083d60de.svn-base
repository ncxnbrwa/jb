package cn.iimedia.jb.classify.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.RankingListBean
import cn.iimedia.jb.http.bean.RankingListData
import com.xiong.appbase.extension.addGridDivider
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.two_level_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by iiMedia on 2018/5/8.
 * 二级分类adapter
 */
class TwoLevelAdapter(val context: Context, val resuleList: ArrayList<ArrayList<Any?>>)
    : RecyclerView.Adapter<TwoLevelAdapter.TwoLevelHolder>() {
    var pool: RecyclerView.RecycledViewPool? = null

    init {
        pool = RecyclerView.RecycledViewPool()
        pool?.setMaxRecycledViews(0, 10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoLevelHolder {
        return TwoLevelHolder(context, pool, LayoutInflater.from(context)
                .inflate(R.layout.two_level_item, parent, false))
    }

    override fun onBindViewHolder(holder: TwoLevelHolder, position: Int) {
        holder.bind(resuleList[position])
    }

    override fun getItemCount(): Int = resuleList.size

    class TwoLevelHolder(val context: Context, pool: RecyclerView.RecycledViewPool?, view: View)
        : RecyclerView.ViewHolder(view) {
        val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
        val els: ELS = ELS.getInstance()
        var layoutManager: GridLayoutManager? = null
        var adapter: ThreeLevelAdapter? = null
        val dataList: ArrayList<RankingListData> = ArrayList()

        init {
            if (layoutManager == null) {
                layoutManager = GridLayoutManager(context, 3)
                layoutManager?.isAutoMeasureEnabled
            }
            itemView.three_level_list.recycledViewPool = pool
            itemView.three_level_list.layoutManager = layoutManager
            itemView.three_level_list.addGridDivider(context, 16)
            if (adapter == null) {
                adapter = ThreeLevelAdapter(context, dataList)
            }
            itemView.three_level_list.adapter = adapter
        }

        fun bind(anyList: ArrayList<Any?>) {
            //设置分类标题
            itemView.title.text = anyList[1].toString()
            dataList.clear()
            val typeId = (anyList[0] as Number).toInt()
            itemView.three_level_list.tag = typeId
            val threeLevelCall = api.getThreeLevel(typeId, els.getStringData(ELS.IMEI))
            threeLevelCall.enqueue(object : Callback<RankingListBean> {
                override fun onFailure(call: Call<RankingListBean>?, t: Throwable?) {
                    DLog.w(Config.HTTP_LOG_TAG, "三级分类请求失败")
                    MyUtils.showToast("榜单信息获取失败")
                }

                override fun onResponse(call: Call<RankingListBean>?, response: Response<RankingListBean>?) {
                    val bean = response?.body()
                    if (((itemView.three_level_list.tag) as Int) == typeId) {
                        if (bean?.code == 1) {
                            //不管有误数据必须刷新adapter,否则会有空数据复用之前的数据问题
                            if (bean.data != null && bean.data.isNotEmpty()) {
                                dataList.addAll(bean.data)
                                Handler(Looper.getMainLooper()).post({
                                    adapter?.notifyDataSetChanged()
                                })
                            } else {
                                Handler(Looper.getMainLooper()).post({
                                    adapter?.notifyDataSetChanged()
                                })
                            }
                        }
                    }
                }
            })

        }
    }
}