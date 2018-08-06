package cn.iimedia.jb.information.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToWeb
import cn.iimedia.jb.http.bean.InfoListData
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.info_detail_item.view.*

/**
 * Created by iiMedia on 2018/6/8.
 * 资讯列表adapter
 */
class InfoListAdapter(val context: Context, val dataList: ArrayList<InfoListData>)
    : RecyclerView.Adapter<InfoListAdapter.InfoHolder>() {
    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        return InfoHolder(context, LayoutInflater.from(context)
                .inflate(R.layout.info_detail_item, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    class InfoHolder(val context: Context, v: View) : RecyclerView.ViewHolder(v) {
        fun bind(data: InfoListData) {
            val title = "VOL.${MyUtils.formatPosition3(adapterPosition + 1)}"
            itemView.info_id.text = title
            itemView.info_title.text = data.title
            itemView.info_img.loadImageFit(data.coverImg)
            itemView.setOnClickListener {
                intentToWeb(context, "http://www.iimedia.cn/mobile${data.no}.html"
                        , title)
            }
        }
    }
}