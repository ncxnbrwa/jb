package cn.iimedia.jb.rankingBrand.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.http.bean.RankingProperties
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.ranking_item.view.*

/**
 * Created by iiMedia on 2018/4/25.
 * 综合评分adapter
 */
class RankingGradeAdapter(val ctx: Context, val screenWidth: Int,
                          val propertisList: ArrayList<RankingProperties>,
                          val itemClickListener: (Int) -> Unit)
    : RecyclerView.Adapter<RankingGradeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.ranking_item, null)
                , screenWidth, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(propertisList[position])
    }

    override fun getItemCount(): Int = propertisList.size

    class ViewHolder(view: View, val screenWidth: Int, val itemClickListener: (Int) -> Unit)
        : RecyclerView.ViewHolder(view) {
        fun bind(property: RankingProperties) {
            itemView.index.text = "${adapterPosition + 1}"
            val lp = itemView.grade_bg.layoutParams
            lp.width = (screenWidth * (property.propertyValue.toDouble()) / 100).toInt()
            itemView.grade_bg.layoutParams = lp
            itemView.crown.visibility = View.GONE
            itemView.ratio.text = MyUtils.formatDouble2(property.propertyValue.toDouble())
            itemView.name.text = property.name

            when (adapterPosition) {
                0 -> {
                    itemView.crown.visibility = View.VISIBLE
                    itemView.crown.setImageResource(R.drawable.gold_crown)
                }
                1 -> {
                    itemView.crown.visibility = View.VISIBLE
                    itemView.crown.setImageResource(R.drawable.silver_crown)
                }
                2 -> {
                    itemView.crown.visibility = View.VISIBLE
                    itemView.crown.setImageResource(R.drawable.copper_crown)
                }
            }
            itemView.setOnClickListener { itemClickListener(position) }
        }
    }
}