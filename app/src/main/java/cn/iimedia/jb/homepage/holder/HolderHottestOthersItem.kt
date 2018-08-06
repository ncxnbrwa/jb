package cn.iimedia.jb.homepage.holder

import android.content.Context
import android.view.View
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.homepage.adapter.HottestListAdapter
import cn.iimedia.jb.http.bean.RankingListData
import com.xiong.appbase.extension.loadImageFit
import kotlinx.android.synthetic.main.hottest_list_item.view.*

/**
 * Created by iiMedia on 2018/5/2.
 * 最热榜单其他项
 */
class HolderHottestOthersItem(val context: Context, view: View) : HottestListAdapter.HottestListHolder(view) {
    override fun bind(listData: RankingListData) {
        itemView.img.loadImageFit(listData.imgUrl)
        itemView.name.text = listData.appName
        itemView.setOnClickListener {
            intentToRankingBrand(context, listData.id.toInt())
        }
    }
}