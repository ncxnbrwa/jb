package cn.iimedia.jb.mine.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.common.intentToForgetPassword
import cn.iimedia.jb.common.intentToRegister
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.RetrofitHelper
import cn.iimedia.jb.http.bean.LoginBean
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.EncryptUtil
import com.xiong.appbase.utils.RegExConstants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bottom_third_party.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by iiMedia on 2018/5/9.
 * 登录页面
 */
class LoginActivity : BaseActivity(), View.OnClickListener {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    private val mActivity = this

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar?.setNavigationOnClickListener { finish() }
        register?.setOnClickListener(this)
        btn_login?.setOnClickListener(this)
        forget_psw?.setOnClickListener(this)
        ll_wechat?.setOnClickListener(this)
        ll_qq?.setOnClickListener(this)
        ll_weibo?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register -> {
                intentToRegister(mActivity)
                finish()
            }
            R.id.btn_login -> loginPhone()
            R.id.forget_psw -> intentToForgetPassword(mActivity)
            R.id.ll_wechat -> {
                RetrofitHelper(mActivity).loginUM(SHARE_MEDIA.WEIXIN)
            }
            R.id.ll_weibo -> {
                RetrofitHelper(mActivity).loginUM(SHARE_MEDIA.SINA)
            }
            R.id.ll_qq -> {
                RetrofitHelper(mActivity).loginUM(SHARE_MEDIA.QQ)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        UMShareAPI.get(mActivity).onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data)
    }

    private fun loginPhone() {
        val phone = et_phone.text.toString()
        val psw = et_psw.text.toString()
        phone_error.visibility = View.GONE
        psw_error.visibility = View.GONE
        if (TextUtils.isEmpty(phone)) {
            phone_error.visibility = View.VISIBLE
            phone_error.text = resources.getString(R.string.phone_empty)
            return
        }
        if (!Regex(RegExConstants.REGEX_MOBILE_EXACT).matches(phone)) {
            phone_error.visibility = View.VISIBLE
            phone_error.text = resources.getString(R.string.phone_not_match)
            return
        }
        if (psw.length !in 6..12) {
            psw_error.visibility = View.VISIBLE
            psw_error.text = resources.getString(R.string.password_not_match)
            return
        }

        //调用登录接口
        showLoadingDialog()
        val loginCall = api.login(phone, EncryptUtil.makeMD5(psw), els.getStringData(ELS.IMEI))
        loginCall.enqueue(object : Callback<LoginBean> {
            override fun onFailure(call: Call<LoginBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, resources.getString(R.string.login_error))
                showToast(resources.getString(R.string.login_error))
                dismissLoadingDialog()
            }

            override fun onResponse(call: Call<LoginBean>?, response: Response<LoginBean>?) {
                val bean = response?.body()
                //code：3；参数错误；具体错误参考msg；2：用户不存在
                //10：用户名或者密码错误；1：成功
                when (bean?.code) {
                    "3" -> showToast(resources.getString(R.string.parameter_error))
                    "2" -> {
                        phone_error.visibility = View.VISIBLE
                        phone_error.text = resources.getString(R.string.user_not_exist)
                    }
                    "10" -> showToast(resources.getString(R.string.name_or_psw_error))
                    "1" -> {
                        els.saveStringData(ELS.USERNAME, bean.nickname?.toString())
                        els.saveStringData(ELS.USER_IMG, bean.img?.toString())
                        els.saveStringData(ELS.USER_ID, bean.userId)
                        els.saveBoolData(ELS.USER_ONLAND, true)
                        els.saveStringData(ELS.PHONE, phone)
                        DLog.w(Config.HTTP_LOG_TAG, "手机登录:$bean")
                        showToast(resources.getString(R.string.login_success))
                        RetrofitHelper(mActivity).initCollectDatabase()
                        finish()
                    }
                }
                dismissLoadingDialog()
            }
        })

    }

    override fun onDestroy() {
        UMShareAPI.get(mActivity).release()
        super.onDestroy()
    }

}