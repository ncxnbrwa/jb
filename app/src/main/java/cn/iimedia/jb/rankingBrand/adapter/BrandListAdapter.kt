package cn.iimedia.jb.rankingBrand.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.common.intentToBrandDetail
import cn.iimedia.jb.common.intentToWeb
import cn.iimedia.jb.http.bean.RankingBrandsList
import com.umeng.analytics.MobclickAgent
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.brand_item.view.*

/**
 * Created by iiMedia on 2018/4/25.
 * 品牌列表adapter
 */
class BrandListAdapter(val ctx: Context, val brandList: ArrayList<RankingBrandsList>) : RecyclerView.Adapter<BrandListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ctx, LayoutInflater.from(ctx).inflate(R.layout.brand_item, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(brandList[position])
    }

    override fun getItemCount(): Int = brandList.size

    class ViewHolder(val ctx: Context, view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: RankingBrandsList) {
            itemView.iv_slash.drawable.level = 0
            itemView.tv_index.text = (adapterPosition + 1).toString()

            itemView.tv_value.text = MyUtils.formatDouble2(data.score.toDouble())
            itemView.tv_brand_name.text = data.brandName
            //商品描述有两种,优先显示brand_rank_desc
            if (data.brand_rank_desc != null && !TextUtils.isEmpty(data.brand_rank_desc.toString().trim())) {
                itemView.tv_desc.text = data.brand_rank_desc.toString()
            } else {
                itemView.tv_desc.text = data.description
            }
            //设置导购逻辑,修改
            if (data.dgList != null && data.dgList.isNotEmpty()) {
                val dg = data.dgList[0]
                itemView.iv_dg.visibility = View.VISIBLE
                when (dg.platform) {
                    1 -> {
                        //京东
                        //用正则式匹配文本获取匹配器
                        if (dg.img != null && !TextUtils.isEmpty(dg.img.trim())) {
                            itemView.iv_dg.loadImageFit(dg.img)
                        } else {
                            itemView.iv_dg.setImageResource(R.mipmap.img_jd)
                        }
                        itemView.iv_dg.setOnClickListener {
                            //友盟导购事件统计
                            val map = HashMap<String, String>()
                            map.put(Config.UM_CLICK_PLAT, Config.UM_PLAT_JD)
                            MobclickAgent.onEvent(ctx, Config.RANKING_UM_ID, map)
                            when (dg.type) {
                                1 -> {
                                    //店铺
                                    if (MyUtils.isAppInstalled(ctx, Config.JD_PACKAGE)) {
                                        if (dg.vId != null) {
                                            MyUtils.gotoJdAppShop(ctx, dg.vId.toString().trim())
                                        } else {
                                            intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                        }
                                    } else {
                                        intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                    }
                                }
                                2 -> {
                                    //商品
                                    if (MyUtils.isAppInstalled(ctx, Config.JD_PACKAGE)) {
                                        if (dg.vId != null) {
                                            MyUtils.gotoJdAppGoods(ctx, dg.vId.toString().trim())
                                        } else {
                                            intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                        }
                                    } else {
                                        intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                    }
                                }
                            }
                        }

                    }
                    2 -> {
                        //天猫
                        if (dg.img != null && !TextUtils.isEmpty(dg.img.trim())) {
                            itemView.iv_dg.loadImageFit(dg.img)
                        } else {
                            itemView.iv_dg.setImageResource(R.mipmap.img_tmall)
                        }
                        itemView.iv_dg.setOnClickListener {
                            val map = HashMap<String, String>()
                            map.put(Config.UM_CLICK_PLAT, Config.UM_PLAT_TM)
                            MobclickAgent.onEvent(ctx, Config.RANKING_UM_ID, map)
                            when (dg.type) {
                                1 -> {
                                    //店铺
                                    if (MyUtils.isAppInstalled(ctx, Config.TMALL_PAKAGE)) {
                                        if (dg.vId != null) {
                                            MyUtils.gotoTmAppShop(ctx, dg.vId.toString().trim())
                                        } else {
                                            intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                        }
                                    } else {
                                        intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                    }
                                    intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                }
                                2 -> {
                                    //商品
                                    if (MyUtils.isAppInstalled(ctx, Config.TMALL_PAKAGE)) {
                                        if (dg.vId != null) {
                                            MyUtils.gotoTmAppGoods(ctx, dg.vId.toString().trim())
                                        } else {
                                            intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                        }
                                    } else {
                                        intentToWeb(ctx, dg.url, Config.DG_TITLE)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                itemView.iv_dg.visibility = View.GONE
            }

            itemView.setOnClickListener {
                intentToBrandDetail(ctx, data.id.toInt())
            }
        }
    }
}