package cn.iimedia.jb.homepage.holder

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import cn.iimedia.jb.R
import cn.iimedia.jb.common.intentToRankingBrand
import cn.iimedia.jb.http.bean.BannerResult
import com.xiong.appbase.extension.loadImageFit
import com.zhouwei.mzbanner.holder.MZViewHolder

/**
 * Created by iiMedia on 2018/7/17.
 * 轮播holder
 */
class BannerViewHolder : MZViewHolder<BannerResult> {
    private var img: ImageView? = null
    override fun onBind(context: Context, position: Int, result: BannerResult?) {
        if (result?.rankInfo?.position0ImgUrl != null
                && !TextUtils.isEmpty(result.rankInfo.position0ImgUrl.toString())) {
            img?.loadImageFit(result.rankInfo.position0ImgUrl.toString())
        } else {
            img?.loadImageFit(result?.rankInfo?.imgUrl!!)
        }
        img?.setOnClickListener {
            intentToRankingBrand(context, (result?.rankInfo?.id!!).toInt())
        }
    }

    override fun createView(context: Context?): View {
        val layout = LayoutInflater.from(context)
                .inflate(R.layout.banner_item, null) as LinearLayout
        img = layout.findViewById<ImageView>(R.id.banner_img)
        return layout
    }
}