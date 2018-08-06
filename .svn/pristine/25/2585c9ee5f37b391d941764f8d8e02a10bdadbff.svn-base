package cn.iimedia.jb.http.bean

/**
 * Created by iiMedia on 2018/4/26.
 * 主页相关实体类
 */
//轮播类
data class BannerBean(val code: String, val result: ArrayList<BannerResult>)

data class BannerResult(
        val posi: Number,
        val rankInfo: RankInfo,
        val skipway: Number,
        val type: Number,
        val vId: Number
)

data class RankInfo(
        val authorId: Number,
        val bGTid: Number,
        val bGTVal: String,
        val createTime: Number,
        val description: String,
        val id: Number,
        val imgUrl: String,
        val name: String,
        val rankCategoryId: Number,
        val status: Number,
        val type: Number,
        val updateTime: Number,
        val position0ImgUrl: Any,
        val position1ImgUrl: Any,
        val appName: String
)

//最新/最热榜单类
data class RankingListBean(
        val code: Number,
        val data: ArrayList<RankingListData>,
        val msg: String
)

data class RankingListData(
        val authorId: Number,
        val bGTid: Number,
        val bGTVal: String,
        val createTime: Number,
        val description: String,
        val id: Number,
        val imgUrl: String,
        val name: String,
        val rankCategoryId: Number,
        val status: Number,
        val type: Number,
        val updateTime: Number,
        val position0ImgUrl: Any,
        val position1ImgUrl: Any,
        val appName: String
)

//小众精选/热门品牌
data class BrandListBean(
        val code: Number,
        val data: ArrayList<BrandListData>,
        val msg: String
)

data class BrandListData(
        val brandname: String,
        val colorLogo: String,
        val grepLogo: String,
        val id: Number,
        val rankInfoId: Number
)

//搜索结果实体类
data class SearchBean(
        val code: String,
        val msg: String,
        val rankInfoList: ArrayList<RankInfo>,
        val brandlist: ArrayList<RankingBrandsList>
)

//专题Item实体类
data class TopicBean(
        val specialList: ArrayList<Special>,
        val code: String,
        val msg: String
)

data class Special(
        val id: Int,
        val title: String,
        val img: String,
        val stat: Int,
        val authorId: Int,
        val createTime: Long,
        val updateTime: Long,
        val colorOne: Any,
        val colorTwo: Any,
        val rankInfoName: ArrayList<String>,
        val sdesc: String
)

//专题详情实体类
data class TopicDetailBean(
        val specialInfo: SpecialInfo,
        val rankInfoList: ArrayList<TopicRankInfo>,
        val code: String,
        val msg: String
)

data class TopicRankInfo(
        val title: Any,
        val sType: Int,
        val img: String,
        val rankType: String,
        val time: Long,
        val bgId: Int
)

data class SpecialInfo(
        val id: Int,
        val title: String,
        val img: String,
        val stat: Int,
        val authorId: Int,
        val createTime: Long,
        val updateTime: Long,
        val colorOne: Any,
        val colorTwo: Any,
        val rankInfoName: Any,
        val sdesc: String
)