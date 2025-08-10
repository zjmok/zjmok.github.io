package com.acadsoc.english.children.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.acadsoc.english.children.app.App;
import com.acadsoc.english.children.util.UiUtils;

/**
 * Created by @Harry on 2017/12/15
 */

public class GridLayoutManagerSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mRow;
    private final int mSpaceHorizontal;
    private int mSpaceVertical;

    /**
     * 理论上会把rv的item压缩变形
     *
     * @param spaceHorizontalDP 水平间隔
     * @param spaceVerticalDP   竖直间隔
     */
    public GridLayoutManagerSpaceItemDecoration(int row, int spaceHorizontalDP, int spaceVerticalDP) {
        mRow = row;
        mSpaceHorizontal = UiUtils.dip2px(App.sContext, spaceHorizontalDP);
        mSpaceVertical = UiUtils.dip2px(App.sContext, spaceVerticalDP);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //左边距
        if (parent.getChildLayoutPosition(view) % mRow == 0) {//每行first左边距设为0
            outRect.left = 0;
        } else {//非最左 设置水平间隔一半
            outRect.left = mSpaceHorizontal >> 1;
        }
        //右
        if (parent.getChildLayoutPosition(view) % mRow == mRow - 1) {//每行lastOne的右边距设为0
            outRect.right = 0;
        } else {//非最右
            outRect.right = mSpaceHorizontal >> 1;
        }
        //上
        outRect.top = 0;
        //下
        outRect.bottom = mSpaceVertical;
    }

}
