package cn.iimedia.jb

import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.APIConstants
import cn.iimedia.jb.http.bean.ContactUsBean
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.custom.CustomTextWatcher
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.MyUtils
import com.xiong.appbase.utils.RegExConstants
import kotlinx.android.synthetic.main.activity_contact_us.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by iiMedia on 2018/5/9.
 * 联系我们页面
 */
class ContactUsActivity : BaseActivity(), View.OnClickListener, View.OnTouchListener {
    private val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    private val els: ELS = ELS.getInstance()

    override fun getLayoutId(): Int = R.layout.activity_contact_us

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setNavigationOnClickListener { finish() }
        btn_commit?.setOnClickListener(this)
        et_message?.setOnTouchListener(this)
        et_message?.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                    msg_limit.text = "${s.length}/1000"
                } else {
                    msg_limit.text = "0/1000"
                }

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> commit()
        }
    }

    private fun commit() {
        val message = et_message.text.toString()
        val name = et_name.text.toString()
        val phone = et_phone.text.toString()
        val email = et_email.text.toString()

        message_error.visibility = View.GONE
        phone_error.visibility = View.GONE
        email_error.visibility = View.GONE
        if (message.length > 1000 || TextUtils.isEmpty(message)) {
            message_error.visibility = View.VISIBLE
            message_error.text = "字数限制1-1000字"
            scroll?.scrollTo(0, 0)
            return
        }
//        if (name.length > 10 || TextUtils.isEmpty(name)) {
//            showToast("您的姓名不能为空")
//            return
//        }
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            showToast("电话和邮箱请必填一项")
            return
        }
        if (!TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            if (!Regex(RegExConstants.REGEX_MOBILE_EXACT).matches(phone)) {
                showPhoneError()
                return
            }
        }
        if (TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email)) {
            if (!Regex(RegExConstants.REGEX_EMAIL).matches(email)) {
                showEmailError()
                return
            }
        }
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email)) {
            if (!Regex(RegExConstants.REGEX_MOBILE_EXACT).matches(phone)) {
                showPhoneError()
                return
            }
            if (!Regex(RegExConstants.REGEX_EMAIL).matches(email)) {
                showEmailError()
                return
            }
        }

        showLoadingDialog()
        val call = api.contactUs(phone, email, name, message, els.getStringData(ELS.IMEI))
        call.enqueue(object : Callback<ContactUsBean> {
            override fun onResponse(call: Call<ContactUsBean>?, response: Response<ContactUsBean>?) {
                val bean = response?.body()
                if (bean?.code == 1) {
                    if (bean.msg.equals("success")) {
                        showToast("提交成功")
                        finish()
                    }
                }
                dismissLoadingDialog()
            }

            override fun onFailure(call: Call<ContactUsBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "联系我们接口请求失败")
                dismissLoadingDialog()
                showToast("提交失败")
            }
        })
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        //解决EditText和ScrollView滑动冲突
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if (view.id == R.id.et_message && MyUtils.canVerticalScroll(et_message)) {
            view.parent.requestDisallowInterceptTouchEvent(true)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }

    private fun showEmailError() {
        email_error.visibility = View.VISIBLE
        email_error.setText(resources.getString(R.string.email_not_match))
    }

    private fun showPhoneError() {
        phone_error.visibility = View.VISIBLE
        phone_error.setText(resources.getString(R.string.phone_not_match))
    }
}