package cn.iimedia.jb.homepage

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import android.widget.TextView
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.common.Config.TOPIC_ID
import cn.iimedia.jb.homepage.adapter.TopicDetailAdapter
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.SpecialInfo
import cn.iimedia.jb.http.bean.TopicDetailBean
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.custom.LinearLayoutManagerWrapper
import com.xiong.appbase.extension.addDivider
import com.xiong.appbase.extension.loadImageFit
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_topic_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/6/1.
 * 专题页
 */
class TopicDetailActivity : BaseActivity(), View.OnClickListener {
    val mActivity = this
    val TAG = "TopicDetailActivity"
    val ANIMATIONS_DURATION = 200L
    var isToolbarVisible = false
    var isTitleLayoutVisible = true
    var toolbarTitle: TextView? = null
    var titleLayout: LinearLayout? = null
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    var sId = 0

    override fun getLayoutId(): Int = R.layout.activity_topic_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarTitle = findViewById(R.id.toolbar_title)
        titleLayout = findViewById(R.id.title_layout)
        sId = intent.extras.getInt(TOPIC_ID)

        toolbar?.setNavigationOnClickListener { finish() }
//        share?.setOnClickListener(this)
        appbar?.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val progress = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat() //百分比从0到1
            //verticalOffset一直为负值,收缩时从0开始,其绝对值增大,展开时其绝对值变小至0
            DLog.e(TAG, "verticalOffset:$verticalOffset")
            animateOnToolbar(progress)
            animateOnTitleLayout(progress)
        }
        //设置顶部图片宽高比
        val imgLp = top_img.layoutParams
        imgLp.height = ScreenUtils.getScreenWidth() / 2
        top_img.layoutParams = imgLp
        //设置头部信息栏固定为屏幕的9/10
        val lp = title_layout?.layoutParams
        lp?.width = ScreenUtils.getScreenWidth() * 9 / 10
        title_layout?.layoutParams = lp

        topic_list?.layoutManager = LinearLayoutManagerWrapper(mActivity)
        topic_list?.isNestedScrollingEnabled = false
        topic_list?.addDivider(mActivity, R.drawable.vertical_white_divider_8dp)
        getData()
    }

    private fun getData() {
        showLoadingDialog()
        val detailCall = api.getTopicDetail(els.getStringData(ELS.IMEI), els.getStringData(ELS.IMEI), sId)
        detailCall.enqueue(object : Callback<TopicDetailBean> {
            override fun onResponse(call: Call<TopicDetailBean>?, response: Response<TopicDetailBean>?) {
                val bean = response?.body()
                when (bean?.code) {
                    "-1" -> showToast(resources.getString(R.string.server_error))
                    "3" -> showToast(resources.getString(R.string.parameter_error))
                    "1" -> {
                        setTopData(bean.specialInfo)
                        val rankInfoList = bean.rankInfoList
                        if (rankInfoList.isNotEmpty()) {
                            topic_list?.adapter = TopicDetailAdapter(mActivity, rankInfoList)
                        }
                    }
                }
                dismissLoadingDialog()
            }

            override fun onFailure(call: Call<TopicDetailBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "专题详情数据获取失败")
                dismissLoadingDialog()
            }
        })
    }

    private fun setTopData(specialInfo: SpecialInfo) {
        //设置列表以外的其他数据
        top_img?.loadImageFit(specialInfo.img)
        toolbar_title?.text = specialInfo.title
        topic_name?.text = specialInfo.title
        topic_content?.text = specialInfo.sdesc
        val color = specialInfo.colorOne
        if (color != null && !TextUtils.isEmpty(color.toString())) {
            title_layout.setBackgroundColor(Color.parseColor(color.toString().replace("#", "#CC")))
            collapsing.setContentScrimColor(Color.parseColor(color.toString()))
        }
        runOnUiThread{

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.share -> {
//                SocialShareActivity.intentTo(mActivity)
//            }
        }
    }

    private fun animateOnToolbar(progress: Float) {
        //处理Toolbar滑动的显示
        if (progress < 0.9f) {
            if (isToolbarVisible) {
                startAlphaAnimation(toolbarTitle as View, View.GONE)
                isToolbarVisible = false
            }
        } else {
            if (!isToolbarVisible) {
                startAlphaAnimation(toolbarTitle as View, View.VISIBLE)
                isToolbarVisible = true
            }
        }
    }

    private fun animateOnTitleLayout(progress: Float) {
        //处理标题Layout滑动的显示
        if (progress >= 0.3f) {
            if (isTitleLayoutVisible) {
                startAlphaAnimation(titleLayout as View, View.GONE)
                isTitleLayoutVisible = false
            }
        } else {
            if (!isTitleLayoutVisible) {
                startAlphaAnimation(titleLayout as View, View.VISIBLE)
                isTitleLayoutVisible = true
            }
        }
    }

    private fun startAlphaAnimation(v: View, visibility: Int) {
        //透明度变化动画,visibility参数表示结束状态
        val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = ANIMATIONS_DURATION
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }
}