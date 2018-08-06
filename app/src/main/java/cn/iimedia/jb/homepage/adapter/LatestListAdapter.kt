package cn.iimedia.jb.homepage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.bean.RankingListData
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.TimeTypeUtils
import kotlinx.android.synthetic.main.latest_list_item.view.*

/**
 * Created by iiMedia on 2018/4/24.
 * 最新榜单Adapter
 */
class LatestListAdapter(val ctx: Context, val dataList: ArrayList<RankingListData>)
    : RecyclerView.Adapter<LatestListAdapter.RankingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        return RankingViewHolder(ctx, LayoutInflater.from(ctx)
                .inflate(R.layout.latest_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    class RankingViewHolder(val ctx: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: RankingListData) {
            itemView.img.loadImageFit(data.imgUrl)
            itemView.name.text = data.appName
            itemView.time.text = TimeTypeUtils.timestamp2yyyyMMdd(data.updateTime.toLong())

            itemView.setOnClickListener {
                intentToRankingBrand(ctx, data.id.toInt())
            }
        }
    }


}