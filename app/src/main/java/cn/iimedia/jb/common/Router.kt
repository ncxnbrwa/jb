package cn.iimedia.jb.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.iimedia.jb.ContactUsActivity
import cn.iimedia.jb.WebActivity
import cn.iimedia.jb.homepage.SearchActivity
import cn.iimedia.jb.homepage.TopicDetailActivity
import cn.iimedia.jb.mine.activity.*
import cn.iimedia.jb.rankingBrand.BrandDetailActivity
import cn.iimedia.jb.rankingBrand.MoreHottestRankingActivity
import cn.iimedia.jb.rankingBrand.RankingBrandActivity

/**
 * Created by iiMedia on 2018/4/28.
 * 页面跳转辅助类
 */
fun intentToBrandDetail(context: Context, id: Int) {
    val intent = Intent(context, BrandDetailActivity::class.java)
    val bundle = Bundle()
    bundle.putInt(Config.BRAND_ID, id)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

fun intentToRankingBrand(context: Context, id: Int) {
    val intent = Intent(context, RankingBrandActivity::class.java)
    val bundle = Bundle()
    bundle.putInt(Config.RANK_INFO_ID, id)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

fun intentToSetting(context: Context) {
    val intent = Intent(context, SettingActivity::class.java)
    context.startActivity(intent)
}

fun intentToUserInfo(context: Context) {
    val intent = Intent(context, UserInfoActivity::class.java)
    context.startActivity(intent)
}

fun intentHottestMore(context: Context) {
    val intent = Intent(context, MoreHottestRankingActivity::class.java)
    context.startActivity(intent)
}

fun intentToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}

fun intentToRegister(context: Context) {
    val intent = Intent(context, RegisterActivity::class.java)
    context.startActivity(intent)
}

fun intentToContactUs(context: Context) {
    val intent = Intent(context, ContactUsActivity::class.java)
    context.startActivity(intent)
}

fun intentToCollect(context: Context) {
    val intent = Intent(context, CollectActivity::class.java)
    context.startActivity(intent)
}

fun intentToModifyPassword(context: Context) {
    val intent = Intent(context, ModifyPasswordActivity::class.java)
    context.startActivity(intent)
}

fun intentToForgetPassword(context: Context) {
    val intent = Intent(context, ForgetPasswordActivity::class.java)
    context.startActivity(intent)
}

fun intentToResetPassword(context: Context, bundle: Bundle) {
    val intent = Intent(context, ResetPasswordActivity::class.java)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

fun intentToSearch(context: Context) {
    val intent = Intent(context, SearchActivity::class.java)
    context.startActivity(intent)
}

fun intentToTopicDetail(context: Context, topicId: Int) {
    val intent = Intent(context, TopicDetailActivity::class.java)
    val bundle = Bundle()
    bundle.putInt(Config.TOPIC_ID, topicId)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

//跳转方式：0：类型；1：品牌详情；2：商品详情
fun intentStyleTo(context: Context, type: Int, rankId: Int, brandId: Int) {
    when (type) {
        1 -> {
            intentToRankingBrand(context, rankId)
        }
        2 -> {
            intentToBrandDetail(context, brandId)
        }
        else -> {
        }
    }
}

fun intentToWeb(context: Context, url: String, title: String) {
    val intent = Intent(context, WebActivity::class.java)
    val bundle = Bundle()
    bundle.putString(Config.WEB_URL, url)
    bundle.putString(Config.WEB_TITLE, title)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

