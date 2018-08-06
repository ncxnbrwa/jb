package cn.iimedia.jb.http.bean

/**
 * Created by iiMedia on 2018/4/27.
 * 品牌榜单实体类
 */
//综合评分
data class RankingPropertyBean(
        val code: Number,
        val data: RankingPropertyData,
        val msg: String
)

data class RankingPropertyData(
        val properties: ArrayList<RankingProperties>,
        val propertyId: Number,
        val propertyName: String,
        val rankInfoId: Number
)

data class RankingProperties(
        val bGDId: Number,
        val name: String,
        val propertyValue: String,
        val type: Number
)

//榜单品牌详情列表
data class RankingBrandListBean(
        var code: Number,
        var msg: String,
        var data: RankingBrandData
)

data class RankingBrandData(
        var rankInfo: RankingBrandRankInfo,
        var brands: ArrayList<RankingBrandsList>
)

data class RankingBrandRankInfo(
        var time: Number,
        var rankInfoName: String,
        var rankInfoDesc: String
)

data class RankingBrandsList(
        var id: Number,
        var parentId: Number,
        var name: String,
        var brandName: String,
        var description: String,
        var mainType: Number,
        var mainTypeName: String,
        var subType: Number,
        var subTypeName: String,
        var colorLogo: String,
        var grepLogo: String,
        var status: Number,
        var authorId: Number,
        var score: Number,
        var imTalk: Number,
        var estTime: String,
        var estAddr: Any,
        var createTime: Number,
        var updateTime: Number,
        var brand_rank_desc: Any,
        val dgList: ArrayList<DgList>
)

//榜单详情
data class RankingDetailBean(
        val code: Number,
        val data: RankingDetailData,
        val msg: String
)

data class RankingDetailData(
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

//品牌详情
data class BrandDetailBean(
        val code: Number,
        val data: BrandDetailData,
        val msg: String
)

data class BrandDetailData(
        val authorId: Number,
        val brandName: String,
        val colorLogo: String,
        val createTime: Number,
        val description: String,
        val estAddr: Any?,
        val estTime: String,
        val grepLogo: String,
        val id: Number,
        val imTalk: Any?,
        val mainType: Number,
        val mainTypeName: String,
        val name: String,
        val parentId: Number,
        val score: Any?,
        val status: Number,
        val subType: Number,
        val subTypeName: String,
        val updateTime: Number
)

//入选榜单
data class RankingSelectBean(
        val code: Number,
        val data: ArrayList<RankingSelectData>,
        val msg: String
)

data class RankingSelectData(
        val authorId: Number,
        val bGTid: Number, //对应品牌ID
        val bGTVal: String,
        val createTime: Number,
        val description: String,
        val id: Number, //对应榜单ID
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

//根据榜单id和品牌id获取品牌排名的实体类
data class RankingIndexBean(
        val code: Number,
        val data: RankingIndexData,
        val msg: String
)

data class RankingIndexData(val ranking: Int = 0)

//品牌详情导购实体类
data class BrandDetailDgBean(
        val dgList: ArrayList<DgList>,
//        val dgList: Any,
        val code: String,
        val msg: String
)

//Platform：1：京东；2：天猫；url:对应的链接；type:1:店铺；2：商品；vId:店铺id、商品id
data class DgList(
        val img: String,
        val platform: Int,
        val title: String,
        val type: Int,
        val url: String,
        val vId: Any
)