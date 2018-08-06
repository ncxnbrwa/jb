package cn.iimedia.jb.http

import android.app.Activity
import android.content.Context
import cn.iimedia.jb.R
import cn.iimedia.jb.common.Config
import cn.iimedia.jb.http.bean.*
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.xiong.appbase.base.BaseActivity
import com.xiong.appbase.custom.TimeButton
import com.xiong.appbase.http.RequestEngine
import com.xiong.appbase.utils.DLog
import com.xiong.appbase.utils.ELS
import com.xiong.appbase.utils.MyUtils
import com.xiong.appbase.utils.MyUtils.showToast
import org.litepal.crud.DataSupport
import org.litepal.crud.callback.FindMultiCallback
import org.litepal.crud.callback.SaveCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by iiMedia on 2018/5/17.
 * 网络请求辅助类
 */
class RetrofitHelper(val context: Context) {
    val api: APIConstants = RequestEngine.createService(APIConstants::class.java)
    val els: ELS = ELS.getInstance()

    fun loginThirdParty(user: User) {
        (context as BaseActivity).showLoadingDialog()
        // imei:手机标识；val:标识对应的值,填昵称;type: 1：微信；2：QQ；3：微博;
        // nickname=昵称；img：头像；device：1：IOS；2：Android；2：web
        val map = HashMap<String, Any>()
        map.put("imei", els.getStringData(ELS.IMEI))
        map.put("val", user.nickname)
        map.put("type", user.platformType)
        map.put("nickname", user.nickname)
        map.put("img", user.avatarUrl)
        map.put("device", 2)
        val thirdPartyCall = api.loginThirdParty(map)
        thirdPartyCall.enqueue(object : Callback<LoginBean> {
            override fun onResponse(call: Call<LoginBean>?, response: Response<LoginBean>?) {
                val bean = response?.body()
                //code：-1：服务器错误；3：参数错误，详情见msg；1：成功
                when (bean?.code) {
                    "-1" -> showToast(context.resources.getString(R.string.server_error))
                    "3" -> showToast(context.resources.getString(R.string.parameter_error))
                    "1" -> {
                        els.saveStringData(ELS.USERNAME, bean.nickname?.toString())
                        els.saveStringData(ELS.USER_IMG, bean.img?.toString())
                        els.saveStringData(ELS.USER_ID, bean.userId)
                        els.saveStringData(ELS.THIRD_PARTY_USER_ID, user.platformUid)
                        els.saveBoolData(ELS.USER_ONLAND_THIRD, true)
                        els.saveIntData(ELS.THIRD_PARTY_PLATFORM, user.platformType)
                        showToast(context.resources.getString(R.string.login_success))
                        initCollectDatabase()
                        (context as Activity).finish()
                        DLog.w(Config.HTTP_LOG_TAG, "第三方登录:$bean")
                    }
                }
                context.dismissLoadingDialog()
            }

            override fun onFailure(call: Call<LoginBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "第三方登录失败")
                showToast(context.resources.getString(R.string.login_error))
                context.dismissLoadingDialog()
            }
        })
    }

    fun initCollectDatabase() {
        val initCollectCall = api.initCollectData(2,
                els.getStringData(ELS.USER_ID), els.getStringData(ELS.IMEI))
        initCollectCall.enqueue(object : Callback<InitCollectBean> {
            override fun onFailure(call: Call<InitCollectBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, "初始化收藏数据失败")
            }

            override fun onResponse(call: Call<InitCollectBean>?, response: Response<InitCollectBean>?) {
                val bean = response?.body()
                if (bean?.code.equals("1")) {
                    val dataList = ArrayList<Int>()
                    val rankList = bean?.rankList
                    val brandList = bean?.brandList
                    if (rankList != null && rankList.isNotEmpty()) {
                        dataList.addAll(rankList)
                    }
                    if (brandList != null && brandList.isNotEmpty()) {
                        dataList.addAll(brandList)
                    }
                    if (dataList.isNotEmpty()) {
                        val hasInitDatabase = els.getBoolData(ELS.HAS_INIT_DATABASE)
                        if (hasInitDatabase) {
                            updateDatabase(dataList)
                        } else {
                            initDatabase(dataList)
                            els.saveBoolData(ELS.HAS_INIT_DATABASE, true)
                        }
                    }
                }
            }
        })
    }

    private fun updateDatabase(dataList: ArrayList<Int>) {
        for (data in dataList) {
            val databaseItem = DataSupport.where("project_id=?", "$data")
                    .find(CollectDatabase::class.java)
            if (databaseItem.isEmpty()) {
                //数据库中不存在该数据,则新建一个
                val collectDatabase1 = CollectDatabase()
                collectDatabase1.project_id = data
                collectDatabase1.save()
            } else {
                //数据库中存在该数据,则更新
                val collectDatabase2 = databaseItem[0]
                collectDatabase2.project_id = data
                collectDatabase2.save()
            }
        }
        logDatabase()
    }

    private fun initDatabase(dataList: ArrayList<Int>) {
        //第一次启动初始化数据库
        val databaseList = ArrayList<CollectDatabase>()
        for (data in dataList) {
            val collectDatabase = CollectDatabase()
            collectDatabase.project_id = data
            databaseList.add(collectDatabase)
        }
        //添加后异步保存到数据库
        DataSupport.saveAllAsync(databaseList).listen(object : SaveCallback {
            override fun onFinish(success: Boolean) {
                logDatabase()
            }
        })
    }

    //友盟第三方登录
    fun loginUM(media: SHARE_MEDIA) {
        UMShareAPI.get(context).getPlatformInfo(context as Activity, media, object : UMAuthListener {
            override fun onStart(platform: SHARE_MEDIA) {
                showToast("授权开始")
                (context as BaseActivity).showLoadingDialog()
            }

            override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {
                showToast("授权完成")
                var platformType = 0

                //platformType: 1：微信；2：QQ；3：微博
                if (platform == SHARE_MEDIA.WEIXIN) {
                    platformType = 1
                } else if (platform == SHARE_MEDIA.SINA) {
                    platformType = 3
                } else if (platform == SHARE_MEDIA.QQ) {
                    platformType = 2
                }
                val uid = data["uid"].toString()
                val headimg = data["iconurl"].toString()
                val nickname = data["name"].toString()
                loginThirdParty(User(nickname, headimg, uid, platformType))
            }

            override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {
                showToast("授权失败" + t.message)
                (context as BaseActivity).dismissLoadingDialog()
            }

            override fun onCancel(platform: SHARE_MEDIA, action: Int) {
                showToast("授权取消")
                (context as BaseActivity).dismissLoadingDialog()
            }
        })
    }

    //获取手机验证码
    fun verificationCode(phone: String, phoneCode: String, timeButton: TimeButton
                         , resultCallback: (Boolean) -> Unit) {
        (context as BaseActivity).showLoadingDialog()
        val sendCodeCall = api.sendCode(phone, 1, phoneCode, els.getStringData(ELS.IMEI))
        sendCodeCall.enqueue(object : Callback<CommonCodeBean> {
            override fun onFailure(call: Call<CommonCodeBean>?, t: Throwable?) {
                DLog.w(Config.HTTP_LOG_TAG, context.resources.getString(R.string.send_code_fail))
                MyUtils.showToast(context.resources.getString(R.string.send_code_fail))
                context.dismissLoadingDialog()
                resultCallback(false)
            }

            override fun onResponse(call: Call<CommonCodeBean>?, response: Response<CommonCodeBean>?) {
                val bean = response?.body()
                when (bean?.code) {
                    "1" -> {
                        MyUtils.showToast(context.resources.getString(R.string.send_code_success))
                        timeButton.startCount()
                        resultCallback(true)
                    }
                    "4" -> {
                        MyUtils.showToast(context.resources.getString(R.string.phone_error))
                    }
                    "9" -> {
                        MyUtils.showToast(context.resources.getString(R.string.phone_registered))
                    }
                    "7" -> {
                        MyUtils.showToast(context.resources.getString(R.string.repeat_send))
                    }
                    "0" -> {
                        MyUtils.showToast(context.resources.getString(R.string.send_code_fail))
                    }
                    "3" -> {
                        MyUtils.showToast(context.resources.getString(R.string.img_code_error))
                    }
                }
                context.dismissLoadingDialog()
            }
        })
    }

    //打印数据库数据
    fun logDatabase() {
        DataSupport.findAllAsync(CollectDatabase::class.java).listen(object : FindMultiCallback {
            override fun <T : Any?> onFinish(t: MutableList<T>?) {
                val list: ArrayList<CollectDatabase> = t as ArrayList<CollectDatabase>
                DLog.w(Config.DATABASE_TAG, list.toString())
            }
        })
    }

}