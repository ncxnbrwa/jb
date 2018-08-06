package cn.iimedia.jb.rankingBrand.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.RankingIndexBean
import cn.iimedia.jb.http.bean.RankingSelectData
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.brand_select_ranking_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/4/26.
 * 品牌详情页入选榜单adapter
 */
class BrandSelectRankingAdapter(val ctx: Context, val dataList: ArrayList<RankingSelectData>
                                , val brandId: Int) : RecyclerView.Adapter<BrandSelectRankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ctx, LayoutInflater.from(ctx).inflate(R.layout.brand_select_ranking_item, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, dataList[position], brandId)
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(val ctx: Context, view: View) : RecyclerView.ViewHolder(view) {
        val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
        val els: ELS = ELS.getInstance()

        fun bind(position: Int, data: RankingSelectData, brandId: Int) {
            val index = position % 4
            when (index) {
                0 -> {
                    itemView.setBackgroundResource(R.mipmap.select_bg_1)
                }
                1 -> {
                    itemView.setBackgroundResource(R.mipmap.select_bg_2)
                }
                2 -> {
                    itemView.setBackgroundResource(R.mipmap.select_bg_3)
                }
                3 -> {
                    itemView.setBackgroundResource(R.mipmap.select_bg_4)
                }
            }
            //请求排名
            val call = api.getRankingIndex(brandId, data.id.toInt(), els.getStringData(ELS.IMEI))
            call.enqueue(object : Callback<RankingIndexBean> {
                override fun onFailure(call: Call<RankingIndexBean>?, t: Throwable?) {
                    DLog.w(Config.HTTP_LOG_TAG, "根据榜单ID,品牌ID获取排名失败")
                }

                override fun onResponse(call: Call<RankingIndexBean>?, response: Response<RankingIndexBean>?) {
                    val bean = response?.body()
                    if (bean?.code == 1) {
                        val ranking = bean.data.ranking
                        if (ranking != 0) {
                            itemView.grade.text = "NO.${ranking}"
                        }
                        itemView.name.text = data.name
                        itemView.setOnClickListener {
                            intentToRankingBrand(ctx, data.id.toInt())
                        }
                    }
                }
            })
        }
    }
}