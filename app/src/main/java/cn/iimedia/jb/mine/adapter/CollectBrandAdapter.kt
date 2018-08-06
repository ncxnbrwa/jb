package cn.iimedia.jb.mine.adapter

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
import com.xiong.appbase.utils.DLog
import kotlinx.android.synthetic.main.collect_brand_item.view.*

/**
 * Created by iiMedia on 2018/5/10.
 * 收藏品牌adapter
 */
class CollectBrandAdapter(val context: Context, val dataList: ArrayList<RankingBrandsList>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CollectBrandHolder && position > 0) {
            holder.bind(dataList[position - 1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        DLog.w("adapter", "$dataList")
        return when (viewType) {
            ViewType.COLLECT_FIRST_OR_LAST -> CollectBrandFirstLastHolder(LayoutInflater.from(context)
                    .inflate(R.layout.first_last_divider, parent, false))
            else -> CollectBrandHolder(context, LayoutInflater.from(context)
                    .inflate(R.layout.collect_brand_item, parent, false))
        }

    }

    override fun getItemCount(): Int = dataList.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
        //第一个和最后一个添加间隔View,在RecyclerView中加不会出现遮挡,在父View中加padding会遮挡
            0, dataList.size + 1 -> ViewType.COLLECT_FIRST_OR_LAST
            else -> ViewType.COLLECT_OTHERS
        }
    }

    class CollectBrandHolder(val context: Context, v: View) : RecyclerView.ViewHolder(v) {
        fun bind(data: RankingBrandsList) {
            itemView.img.loadImageFit(data.colorLogo)
            itemView.name.text = data.brandName
            itemView.setOnClickListener { intentToBrandDetail(context, data.id.toInt()) }
        }
    }

    class CollectBrandFirstLastHolder(v: View) : RecyclerView.ViewHolder(v)
}