package cn.iimedia.jb.mine.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.CommonCodeBean
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.EncryptUtil
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/10.
 * 找回密码下一步
 */
class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()
    private var phone = ""
    private var code = ""

    override fun getLayoutId(): Int = R.layout.activity_reset_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setNavigationOnClickListener { finish() }
        btn_last.setOnClickListener(this)
        btn_ok.setOnClickListener(this)
        //获取传值
        val bundle = intent.extras
        phone = bundle.getString(Config.RESET_PSW_PHONE)
        code = bundle.getString(Config.RESET_PSW_CODE)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_last -> finish()
            R.id.btn_ok -> commit()
        }
    }

    private fun commit() {
        val newPsw = et_new_psw.text.toString()
        val confirmPsw = et_confirm_psw.text.toString()

        new_psw_error.visibility = View.GONE
        confirm_psw_error.visibility = View.GONE
        if (newPsw.length !in 6..12) {
            new_psw_error.visibility = View.VISIBLE
            new_psw_error.text = resources.getString(R.string.password_not_match)
            return
        }
        if (TextUtils.isEmpty(confirmPsw)) {
            confirm_psw_error.visibility = View.VISIBLE
            confirm_psw_error.text = resources.getString(R.string.confirm_password_empty)
            return
        }
        if (!newPsw.equals(confirmPsw)) {
            confirm_psw_error.visibility = View.VISIBLE
            confirm_psw_error.text = resources.getString(R.string.password_not_same)
            return
        }

        showLoadingDialog()
        val changePswCall = api.resetPassword(phone, EncryptUtil.makeMD5(newPsw),
                code, els.getStringData(ELS.IMEI))
        changePswCall.enqueue(object : Callback<CommonCodeBean> {
            override fun onFailure(call: Call<CommonCodeBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, resources.getString(R.string.change_psw_fail))
                dismissLoadingDialog()
                showToast(resources.getString(R.string.change_psw_fail))
            }

            override fun onResponse(call: Call<CommonCodeBean>?, response: Response<CommonCodeBean>?) {
                val bean = response?.body()
                //-1：服务器修改出错。3：参数错误；详细参考msg；
                // 2：号码没有注册；请先注册；1：修改成功；
                when (bean?.code) {
                    "-1" -> showToast(resources.getString(R.string.server_error))
                    "3" -> showToast(resources.getString(R.string.parameter_error))
                    "2" -> showToast(resources.getString(R.string.pls_register))
                    "1" -> {
                        showToast("重置成功")
                        finish()
                        mApp.finishSpecialActivity(ForgetPasswordActivity::class.java)
                    }
                }
                dismissLoadingDialog()
            }
        })
    }
}