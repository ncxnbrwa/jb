package cn.iimedia.jb.common;

import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;

import cn.iimedia.jb.http.bean.BannerResult;

/**
 * Created by iiMedia on 2018/7/17.
 */

public class CommonUtils {
    //因为Kotlin泛型和java的矛盾,所以在此写个方法提供轮播数据
    public static void setBanner(MZBannerView bannerView, ArrayList<BannerResult> datas
            , MZHolderCreator mzHolderCreator) {
        bannerView.setPages(datas, mzHolderCreator);
    }


}
