package cn.iimedia.jb.classify.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.bean.RankingListData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.three_level_item.view.*

/**
 * Created by iiMedia on 2018/5/9.
 * 根据二级分类查找的榜单信息,三级分类Adapter
 */
class ThreeLevelAdapter(val context: Context, val dataList: ArrayList<RankingListData>) : RecyclerView.Adapter<ThreeLevelAdapter.ThreeLevelHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreeLevelHolder {
        return ThreeLevelHolder(context, LayoutInflater.from(context).inflate(R.layout.three_level_item, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ThreeLevelHolder, position: Int) {
        holder.bind(dataList[position])
    }

    class ThreeLevelHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: RankingListData) {
//            itemView.img.loadImageFit(data.imgUrl)
            val img = itemView.img
            if (!TextUtils.isEmpty(data.imgUrl)) {
                img.tag = data.imgUrl
                Glide.with(context).load(data.imgUrl)
                        .into(object : SimpleTarget<Drawable>() {
                            override fun onResourceReady(p0: Drawable, p1: Transition<in Drawable>?) {
                                //图片加载完成后判断是指定的TAG url,再去设置到控件中
                                if (img.tag.toString().equals(data.imgUrl)) {
                                    img.setImageDrawable(p0)
                                }
                            }
                        })
            } else {
                img.setImageResource(R.mipmap.zhanweitu)
            }

            itemView.name.text = data.appName
            itemView.setOnClickListener {
                intentToRankingBrand(context, data.id.toInt())
            }
        }
    }
}
