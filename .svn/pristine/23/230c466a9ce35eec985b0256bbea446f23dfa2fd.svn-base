package cn.iimedia.jb.homepage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.ViewType
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.bean.RankInfo
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.search_result_head.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*


/**
 * Created by iiMedia on 2018/5/21.
 * 搜索结果榜单adapter
 */
class SearchRankingAdapter(val context: Context, val rankInfoList: ArrayList<RankInfo>
                           , var key: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TYPE_SEARCH_HEADER -> SearchRankingHeadHolder(LayoutInflater.from(context)
                    .inflate(R.layout.search_result_head, parent, false))
            else -> SearchRankingItemHolder(LayoutInflater.from(context)
                    .inflate(R.layout.search_result_item, parent, false), key, context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchRankingHeadHolder && position == 0) {
            holder.bindHead()
        }
        if (holder is SearchRankingItemHolder && position > 0) {
            val data = rankInfoList[position - 1]
            holder.itemView.img.loadImageFit(data.imgUrl)
            holder.itemView.name.text = MyUtils.getSpannableForeStr(key, data.name)
            holder.itemView.setOnClickListener { intentToRankingBrand(context, (data.id).toInt()) }
        }
    }

    override fun getItemCount(): Int = rankInfoList.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.TYPE_SEARCH_HEADER
            else -> ViewType.TYPE_SEARCH_ITEM
        }
    }

    class SearchRankingHeadHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindHead() {
            itemView.type.text = "榜单"
        }
    }

    class SearchRankingItemHolder(view: View, val key: String, val context: Context)
        : RecyclerView.ViewHolder(view) {
        fun bindItem(data: RankInfo) {}
    }

}