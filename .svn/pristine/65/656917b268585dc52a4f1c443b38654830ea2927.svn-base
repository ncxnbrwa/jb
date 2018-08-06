package cn.iimedia.jb.rankingBrand.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.bean.RankingListData
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.more_hottest_item.view.*

/**
 * Created by iiMedia on 2018/5/7.
 * 最热榜单更多页面adapter
 */
class MoreHottestAdapter(val context: Context, val dataList: ArrayList<RankingListData>)
    : RecyclerView.Adapter<MoreHottestAdapter.MoreHottestHolder>() {

    override fun onBindViewHolder(holder: MoreHottestHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreHottestHolder {
        return MoreHottestHolder(context, LayoutInflater.from(context).inflate(R.layout.more_hottest_item, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    class MoreHottestHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: RankingListData) {
            itemView.index.text = "${MyUtils.formatPosition2(adapterPosition + 1)}"
            when (adapterPosition) {
                0 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.more_rankingbg_color1))
                1 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.more_rankingbg_color2))
                2 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.more_rankingbg_color3))
                else -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.more_rankingbg_color4))
            }
            itemView.ranking_type.text = data.appName
            itemView.setOnClickListener {
                intentToRankingBrand(context, data.id.toInt())
            }
        }
    }
}