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
import kotlinx.android.synthetic.main.activity_modify_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/10.
 * 修改密码
 */
class ModifyPasswordActivity : BaseActivity(), View.OnClickListener {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()

    override fun getLayoutId(): Int = R.layout.activity_modify_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setNavigationOnClickListener { finish() }
        btn_confirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_confirm -> commit()
        }
    }

    private fun commit() {
        val oldPsw = et_old_psw.text.toString()
        val newPsw = et_new_psw.text.toString()
        val confirmPsw = et_confirm_psw.text.toString()

        old_psw_error.visibility = View.GONE
        new_psw_error.visibility = View.GONE
        confirm_psw_error.visibility = View.GONE
        if (TextUtils.isEmpty(oldPsw)) {
            old_psw_error.visibility = View.VISIBLE
            old_psw_error.text = resources.getString(R.string.old_password_empty)
            return
        }
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
        val modifyPswCall = api.modifyPassword(EncryptUtil.makeMD5(oldPsw), EncryptUtil.makeMD5(newPsw),
                els.getStringData(ELS.USER_ID), els.getStringData(ELS.IMEI))
        modifyPswCall.enqueue(object : Callback<CommonCodeBean> {
            override fun onFailure(call: Call<CommonCodeBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, resources.getString(R.string.change_psw_fail))
                dismissLoadingDialog()
                showToast(resources.getString(R.string.change_psw_fail))
            }

            override fun onResponse(call: Call<CommonCodeBean>?, response: Response<CommonCodeBean>?) {
                val bean = response?.body()
                // -1：修改失败；3：参数错误，详细错误参考msg；
                // 2：用户不存在；10：手机号或者密码错误。1：成功；
                when (bean?.code) {
                    "-1" -> showToast(resources.getString(R.string.server_error))
                    "3" -> showToast(resources.getString(R.string.parameter_error))
                    "2" -> showToast(resources.getString(R.string.pls_register))
                    "10"-> {
                        old_psw_error.visibility = View.VISIBLE
                        old_psw_error.text = "原密码错误"
                    }
                    "1" -> {
                        showToast(resources.getString(R.string.change_psw_success))
                        finish()
                    }
                }
                dismissLoadingDialog()
            }
        })

    }
}