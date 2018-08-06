package cn.iimedia.jb.homepage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.ViewType
import cn.iimedia.jb.common.intentToBrandDetail
import cn.iimedia.jb.http.bean.RankingBrandsList
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.search_result_head.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*

/**
 * Created by iiMedia on 2018/5/21.
 * 搜索结果品牌adapter
 */
class SearchBrandAdapter(val context: Context, val brandList: ArrayList<RankingBrandsList>
                         , var key: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TYPE_SEARCH_HEADER -> SearchBrandHeadHolder(LayoutInflater.from(context)
                    .inflate(R.layout.search_result_head, parent, false))
            else -> SearchBrandItemHolder(LayoutInflater.from(context)
                    .inflate(R.layout.search_result_item, parent, false), key, context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchBrandHeadHolder && position == 0) {
            holder.bindHead()
        }
        if (holder is SearchBrandItemHolder && position > 0) {
            val data = brandList[position - 1]
            holder.itemView.img.loadImageFit(data.colorLogo)
            holder.itemView.name.text = MyUtils.getSpannableForeStr(key, data.brandName)
            holder.itemView.setOnClickListener { intentToBrandDetail(context, (data.id).toInt()) }
        }
    }

    //    override fun getItemCount(): Int = brandList.size + 1
    override fun getItemCount(): Int = 6

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.TYPE_SEARCH_HEADER
            else -> ViewType.TYPE_SEARCH_ITEM
        }
    }

    class SearchBrandHeadHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindHead() {
            itemView.type.text = "品牌"
        }
    }

    class SearchBrandItemHolder(view: View, val key: String, val context: Context)
        : RecyclerView.ViewHolder(view) {
        fun bindItem(data: RankingBrandsList) {}
    }

}