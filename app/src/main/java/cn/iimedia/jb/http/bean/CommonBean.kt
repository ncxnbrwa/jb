package cn.iimedia.jb.http.bean

/**
 * Created by iiMedia on 2018/5/14.
 * 一些通用实体类
 */
data class CommonCodeBean(val code: String, val msg: String)

data class CommonUserBean(val userId: String, val code: String, val msg: String)

data class CommonResultBean(val result: String, val code: String, val msg: String)

data class RegisterBean(val userId: String, val code: String, val msg: String)

//nickname，用户昵称；userId：用户标识；img：用户头像
data class LoginBean(
        val code: String,
        val img: Any?,
        val msg: String,
        val nickname: Any?,
        val userId: String
)

//第三方登录临时封装类
data class User(
        val nickname:String,
        val avatarUrl:String,
        val platformUid:String,
        val platformType:Int
)

