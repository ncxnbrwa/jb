package cn.iimedia.jb

import android.os.Bundle
import cn.iimedia.jb.common.Config
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.utils.DLog
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Created by iiMedia on 2018/6/19.
 * 通用网页加载页面
 */
class WebActivity : BaseActivity() {
    var url: String = ""
    val TAG = "WebActivity"
    var title = ""

    override fun getLayoutId(): Int = R.layout.activity_web

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.extras.getString(Config.WEB_URL, "")
        title = intent.extras.getString(Config.WEB_TITLE, "")
        toolbar.setNavigationOnClickListener { finish() }
        tv_title.text = title
        DLog.w(TAG, "加载的url:$url")
        web?.loadUrl(url)
    }

    override fun onBackPressedSupport() {
        if (!web.goBack()) {
            super.onBackPressedSupport()
        }
    }

    override fun onDestroy() {
        web?.destroy()
        super.onDestroy()
    }
}