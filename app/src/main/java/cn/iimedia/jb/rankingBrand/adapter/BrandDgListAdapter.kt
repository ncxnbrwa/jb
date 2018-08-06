package cn.iimedia.jb.rankingBrand.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.common.intentToWeb
import cn.iimedia.jb.http.bean.DgList
import com.umeng.analytics.MobclickAgent
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.utils.MyUtils
import kotlinx.android.synthetic.main.brand_dg_item.view.*

/**
 * Created by iiMedia on 2018/6/21.
 * 品牌详情导购adapter
 */
class BrandDgListAdapter(val context: Context, val dgList: ArrayList<DgList>)
    : RecyclerView.Adapter<BrandDgListAdapter.BrandDgHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandDgHolder {
        return BrandDgHolder(LayoutInflater.from(context)
                .inflate(R.layout.brand_dg_item, parent, false), context)
    }

    override fun getItemCount(): Int = dgList.size

    override fun onBindViewHolder(holder: BrandDgHolder, position: Int) {
        holder.bind(dgList[position])
    }

    class BrandDgHolder(view: View, val ctx: Context) : RecyclerView.ViewHolder(view) {
        fun bind(dg: DgList) {
            when (dg.platform) {
                1 -> {
                    //京东
                    if (dg.img != null && !TextUtils.isEmpty(dg.img.trim())) {
                        itemView.img.loadImageFit(dg.img)
                    } else {
                        itemView.img.setImageResource(R.mipmap.img_jd)
                    }
                    itemView.setOnClickListener {
                        val map = HashMap<String, String>()
                        map.put(Config.UM_CLICK_PLAT, Config.UM_PLAT_JD)
                        MobclickAgent.onEvent(ctx, Config.BRAND_UM_ID, map)
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
                        itemView.img.loadImageFit(dg.img)
                    } else {
                        itemView.img.setImageResource(R.mipmap.img_tmall)
                    }
                    itemView.setOnClickListener {
                        val map = HashMap<String, String>()
                        map.put(Config.UM_CLICK_PLAT, Config.UM_PLAT_TM)
                        MobclickAgent.onEvent(ctx, Config.BRAND_UM_ID, map)
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
        }
    }
}