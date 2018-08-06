package cn.iimedia.jb.rankingBrand.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R

/**
 * Created by iiMedia on 2018/4/26.
 * 品牌详情页上榜商品adapter
 */
class BrandGoodsAdapter(val ctx: Context) : RecyclerView.Adapter<BrandGoodsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.brand_goods_item, null))
    }

    override fun getItemCount(): Int = 4

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {

        }
    }
}