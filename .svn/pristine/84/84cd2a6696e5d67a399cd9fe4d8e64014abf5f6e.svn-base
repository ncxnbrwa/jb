package cn.iimedia.jb.classify.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToBrandDetail
import cn.iimedia.jb.http.bean.BrandListData
import com.xiong.appbase.extension.loadImageFit
import kotlinx.android.synthetic.main.three_level_item.view.*

/**
 * Created by iiMedia on 2018/5/25.
 * 热门品牌adapter
 */
class HotBrandAdapter(val context: Context, val dataList: ArrayList<BrandListData>)
    : RecyclerView.Adapter<HotBrandAdapter.HotBrandHolder>() {

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HotBrandHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotBrandHolder {
        return HotBrandHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.three_level_item, parent, false))
    }

    class HotBrandHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: BrandListData) {
            itemView.img.loadImageFit(data.colorLogo)
            itemView.name.text = (data.brandname).split(" ")[0]
            itemView.setOnClickListener { intentToBrandDetail(context, data.id.toInt()) }
        }
    }
}