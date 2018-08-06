package cn.iimedia.jb.homepage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToBrandDetail
import cn.iimedia.jb.http.bean.BrandListData
import com.xiong.appbase.extension.loadImageFit
import kotlinx.android.synthetic.main.featured_brand_item.view.*

/**
 * Created by iiMedia on 2018/4/24.
 * 小众精选adapter
 */
class FeatureBrandAdapter(val ctx: Context, val dataList: ArrayList<BrandListData>) : RecyclerView.Adapter<FeatureBrandAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ctx, LayoutInflater.from(ctx).inflate(R.layout.featured_brand_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    class ViewHolder(val ctx: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: BrandListData) {
            itemView.img.loadImageFit(data.colorLogo)
            itemView.setOnClickListener {
                intentToBrandDetail(ctx, data.id.toInt())
            }
        }
    }
}