package cn.iimedia.jb.homepage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.homepage.holder.HolderHottestLastItem
import cn.iimedia.jb.homepage.holder.HolderHottestOthersItem
import cn.iimedia.jb.http.bean.RankingListData

/**
 * Created by iiMedia on 2018/4/24.
 * 最热榜单adapter
 */
class HottestListAdapter(val context: Context, val dataList: ArrayList<RankingListData>)
    : RecyclerView.Adapter<HottestListAdapter.HottestListHolder>() {
    val LAST_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HottestListHolder {
        when (viewType) {
            LAST_ITEM -> {
                return HolderHottestLastItem(context, LayoutInflater.from(context).inflate(R.layout.more_item,
                        parent, false))
            }
            else -> {
                return HolderHottestOthersItem(context, LayoutInflater.from(context).inflate(R.layout.hottest_list_item,
                        parent, false))
            }
        }

    }

    override fun onBindViewHolder(holder: HottestListHolder, position: Int) {
        val listData: RankingListData
        //因为加了底部布局,所以防止集合下标越界
        if (position < dataList.size) {
            listData = dataList[position]
        } else {
            listData = dataList[dataList.size - 1]
        }
        holder.bind(listData)
    }

    override fun getItemCount(): Int = dataList.size + 1

    override fun getItemViewType(position: Int): Int {
        when (position) {
            dataList.size -> {
                return LAST_ITEM
            }
            else -> {
                return super.getItemViewType(position)
            }
        }
    }

    open class HottestListHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(listData: RankingListData) {
        }
    }

}