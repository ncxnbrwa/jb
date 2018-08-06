package cn.iimedia.jb.rankingBrand.scrollPage;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.lang.ref.WeakReference;

import cn.iimedia.jb.R;

/**
 * Created by iiMedia on 2018/2/30.
 */

public class PageBehavior extends CoordinatorLayout.Behavior {
    private static final int PAGE_ONE = 1;
    private static final int PAGE_TWO = 2;
    private WeakReference<View> dependentView, childView;
    private Scroller scroller;
    private Handler handler;
    private int status = PAGE_ONE;//记录状态
    private float gap;
    private int totalOne = 0;
    private int scrollY = 0;
    private int mode = 0;

    public OnPageChanged mOnPageChanged;

    public void setOnPageChanged(OnPageChanged onPageChanged) {
        mOnPageChanged = onPageChanged;
    }

    public PageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        handler = new Handler();
        gap = context.getResources().getDimensionPixelSize(R.dimen.gap);
    }

    /**
     * 确定被观察者的方法
     *
     * @param parent     嵌套的父CoordinatorLayout
     * @param child      观察者,即设置behavior的View
     * @param dependency 被观察者View
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //依赖第一个ScrollView
        if (dependency.getId() == R.id.pageOne) {
            dependentView = new WeakReference<>(dependency);
            childView = new WeakReference<>(child);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        //child的布局
        child.layout(0, 0, parent.getWidth(), child.getMeasuredHeight());
        return true;
    }

    /**
     * 被观察者View变化时回调方法
     *
     * @param parent     同上
     * @param child      同上
     * @param dependency 同上
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //让child随着依赖视图的属性变动而变动
        //保持第二个ScrollView一直在第一个ScrollView下面
        child.setTranslationY(dependency.getMeasuredHeight() + dependency.getTranslationY());
        return true;
    }

    /**
     * @param coordinatorLayout 最外层CoordinatorLayout
     * @param child             同上
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes  滑动方向
     * @return true处理这次滑动
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        scroller.abortAnimation();//停止动画
        //只监测Vertical方向的动作
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * @param coordinatorLayout 最外层CoordinatorLayout
     * @param child             同上
     * @param target
     * @param dxConsumed        是滑动时水平方向消耗的距离,手指左移为正,右移为负
     * @param dyConsumed        是滑动时竖直方向消耗的距离,手指上滑为正,下滑为负
     * @param dxUnconsumed      指水平超出视图位置的消费距离,手指左移为正,右移为负
     * @param dyUnconsumed      指竖直超出视图位置的消费距离,一般为用户请求,手指上滑为正,下滑为负
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (status == PAGE_ONE) { //当前为第一个ScrollView
            if (dyUnconsumed > 0) {
                //此时是上拉的临界点
                View dependentView = getDependentView();
                totalOne = (totalOne - dyUnconsumed);
                //此时totalOne为负值,所以会向上偏移,乘以0.5让偏移看起来较小
                dependentView.setTranslationY(totalOne * 0.5f);
            }

//            if (dependentView.get().getTranslationY() < 0 && dyConsumed < 0) {
//                //手指上拉时发生下移,禁止滑动
//                mode = 1;
//                ((Page) dependentView.get()).setScrollAble(false, dependentView.get().getTranslationY());
//            }
        } else if (status == PAGE_TWO) {
            if (dyUnconsumed < 0) {
                //第二个ScrollView下拉的临界点
                View dependentView = getDependentView();
                //算出已下拉出的距离
                totalOne = (totalOne + dyUnconsumed);
                //用第一个ScrollView的高总值减去下拉出的距离
                int newTranslate = (int) (-dependentView.getMeasuredHeight() - (totalOne * 0.5));
                dependentView.setTranslationY(newTranslate);
            }

//            if (dependentView.get().getTranslationY() > -dependentView.get().getHeight() && dyConsumed > 0) {
//                //手指下拉时发生上移,禁止滑动
//                mode = 1;
//                ((Page) child).setScrollAble(false, dependentView.get().getTranslationY());
//            }
        }
    }

    /**
     * 滑动停止时回调
     *
     * @param coordinatorLayout 同上
     * @param child             同上
     * @param target
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        totalOne = 0;
        View dependentView = getDependentView();
        float translationY = dependentView.getTranslationY();
        if (status == PAGE_ONE) {
            //在第一页
            if (mode == 0) {
                if (translationY < -gap) {
                    //翻页,利用Scroll记录偏移量开始值
                    scroller.startScroll(0, (int) translationY
                            , 0, (int) (-dependentView.getMeasuredHeight() - translationY));
                    status = PAGE_TWO;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toBottom();
                    }
                } else {
                    //回弹第一页
                    scroller.startScroll(0, (int) translationY, 0, (int) (-translationY));
                }
            } else {
                //手指上拉时发生下移
                mode = 0;
                if (scrollY < -gap) {
                    scroller.startScroll(0, scrollY
                            , 0, -dependentView.getMeasuredHeight() - scrollY);
                    status = PAGE_TWO;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toBottom();
                    }
                } else {
                    scroller.startScroll(0, scrollY, 0, -scrollY);
                }
            }
        } else if (status == PAGE_TWO) {
            //在第二页
            if (mode == 0) {
                if (translationY < -dependentView.getMeasuredHeight() + gap) {
                    scroller.startScroll(0, (int) translationY
                            , 0, (int) (-dependentView.getMeasuredHeight() - translationY));
                } else {
                    scroller.startScroll(0, (int) translationY, 0, (int) (-translationY));
                    status = PAGE_ONE;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toTop();
                    }
                }
            } else {
                //手指下拉时发生上移
                mode = 0;
                if (scrollY < -dependentView.getMeasuredHeight() + gap) {
                    scroller.startScroll(0, scrollY
                            , 0, -dependentView.getMeasuredHeight() - scrollY);
                } else {
                    //回上一页
                    scroller.startScroll(0, scrollY, 0, -scrollY);
                    status = PAGE_ONE;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toTop();
                    }
                }
            }
        }

        //执行回弹动画
        handler.post(new Running());
    }

    private View getDependentView() {
        return dependentView.get();
    }

    private class Running implements Runnable {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                View dependentView = getDependentView();
                dependentView.setTranslationY(scroller.getCurrY());
                handler.post(this);
            } else {
            }
        }
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public void backToTop() {
        View dependentView = getDependentView();
        float translationY = dependentView.getTranslationY();
        if (status == PAGE_TWO) {
            if (mode == 0) {
                childView.get().setScrollY(0);
                dependentView.setScrollY(0);
                scroller.startScroll(0, (int) translationY, 0, (int) (-translationY));
                status = PAGE_ONE;
                if (mOnPageChanged != null) {
                    mOnPageChanged.toTop();
                }
            }
        }

        handler.post(new Running());
    }


    public void scrollToBottom() {
        View dependentView = getDependentView();
        float translationY = dependentView.getTranslationY();
        dependentView.setScrollY(dependentView.getMeasuredHeight());
        if (status == PAGE_ONE) {
            if (mode == 0) {
                scroller.startScroll(0, (int) translationY, 0
                        , (int) (-dependentView.getMeasuredHeight() - translationY));
                status = PAGE_TWO;
                if (mOnPageChanged != null) {
                    mOnPageChanged.toBottom();
                }
            }
        }

        handler.post(new Running());
    }

    public interface OnPageChanged {

        void toTop();

        void toBottom();
    }
}
