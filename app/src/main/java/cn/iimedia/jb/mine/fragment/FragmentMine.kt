package cn.iimedia.jb.mine.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.*
import com.xiong.appbase.base.BaseFragment
import com.xiong.appbase.extension.loadAvatarFit
import com.xiong.appbase.utils.ELS
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by iiMedia on 2018/5/3.
 * 我的页面
 */
class FragmentMine : BaseFragment(), View.OnClickListener {
    val els = ELS.getInstance()

    companion object {
        fun getInstance(): FragmentMine {
            val fragment = FragmentMine()
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBar(R.color.bg_color8)
        user_icon.setOnClickListener(this)
        user_name.setOnClickListener(this)
        collect_layout.setOnClickListener(this)
        contact_us_layout.setOnClickListener(this)
        setting.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        updateUserInfo()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.user_icon, R.id.user_name -> {
                if (!els.getBoolData(ELS.USER_ONLAND) && !els.getBoolData(ELS.USER_ONLAND_THIRD)) {
                    //未登录跳转登录页
                    intentToLogin(mActivity)
                } else {
                    //登录跳转个人资料页
                    intentToUserInfo(mActivity)
                }
            }
            R.id.collect_layout -> {
                if (!els.getBoolData(ELS.USER_ONLAND) && !els.getBoolData(ELS.USER_ONLAND_THIRD)) {
                    intentToLogin(mActivity)
                } else {
                    intentToCollect(mActivity)
                }
            }
            R.id.contact_us_layout -> intentToContactUs(mActivity)
            R.id.setting -> intentToSetting(mActivity)
        }

    }

    private fun updateUserInfo() {
        //设置头部登录状态
        if (els.getBoolData(ELS.USER_ONLAND) || els.getBoolData(ELS.USER_ONLAND_THIRD)) {
            user_icon.loadAvatarFit(els.getStringData(ELS.USER_IMG))
            user_name.text = els.getStringData(ELS.USERNAME)
            top_layout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color4))
            setStatusBarColor(R.color.bg_color4)
        } else {
            user_icon.setImageResource(R.mipmap.avatar)
            user_name.text = resources.getString(R.string.logout_text)
            top_layout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color8))
            setStatusBarColor(R.color.bg_color8)
        }
    }
}