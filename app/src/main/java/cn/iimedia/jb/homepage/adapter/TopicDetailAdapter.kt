package cn.iimedia.jb.homepage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.bean.TopicRankInfo
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.TimeTypeUtils
import kotlinx.android.synthetic.main.latest_list_item.view.*

/**
 * Created by iiMedia on 2018/6/6.
 * 专题详情
 */
class TopicDetailAdapter(val context: Context, val rankInfoList: ArrayList<TopicRankInfo>)
    : RecyclerView.Adapter<TopicDetailAdapter.TopicHolder>() {

    override fun getItemCount(): Int = rankInfoList.size

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        holder.bind(rankInfoList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        return TopicHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.latest_list_item, parent, false))
    }

    class TopicHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: TopicRankInfo) {
            itemView.img.loadImageFit(data.img)
            itemView.name.text = data.title.toString()
            itemView.time.text = TimeTypeUtils.timestamp2yyyyMMdd(data.time)
            itemView.setOnClickListener { intentToRankingBrand(context, data.bgId) }
        }
    }
}