package cn.iimedia.jb.http.bean

/**
 * Created by iiMedia on 2018/5/9.
 * 和用户数据相关的实体类
 */
//联系我们
data class ContactUsBean(
        val code: Number,
        val data: Any?,
        val msg: String
)

//我的收藏榜单
data class CollectRankingBean(
        val code: String,
        val msg: String,
        val result: ArrayList<RankInfo>
)

//我的收藏品牌
data class CollectBrandBean(
        val code: String,
        val msg: String,
        val result: ArrayList<RankingBrandsList>
)

//初始化收藏类
data class InitCollectBean(
        val code: String,
        val msg: String,
        val brandList: ArrayList<Int>,
        val rankList: ArrayList<Int>
)
