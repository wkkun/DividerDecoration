package com.wkkun.divider
import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.*

/**
 *Date:2019/7/10
 *Author:kunkun.wang
 *Des:线性布局分割线 注意该分割线 只能用在LinerLayoutManager 网格布局请使用网格分割线
 **/
class LinerItemDecoration(builder: Builder) : BaseItemDecoration(builder) {

    override fun setItemOffsets(position: Int, itemCount: Int, outRect: Rect, view: View, parent: RecyclerView) {
        if (!isShowLastDivider && (position == itemCount - 1)) {
            outRect.set(0, 0, 0, 0)
            return
        }
        if (orientation == OrientationHelper.VERTICAL) {
            //纵向
            outRect.set(
                0, 0, 0,
                dividerSpaceProvider?.getDividerSpace(position, parent) ?: margin[3] + getDrawableHeight(
                    position,
                    parent
                ) + margin[1]
            )
        } else {
            //横向
            outRect.set(
                0, 0,
                dividerSpaceProvider?.getDividerSpace(position, parent) ?: margin[2] + getDrawableHeight(
                    position,
                    parent
                ) + margin[0], 0
            )
        }
    }

    override fun getDrawRectBound(position: Int, itemCount: Int, view: View, parent: RecyclerView): ArrayList<Rect> {
        val rectBound = Rect()
        parent.getDecoratedBoundsWithMargins(view, rectBound)
        //设置分割线的绘制区域
        if (orientation == OrientationHelper.VERTICAL) {
            //纵向
            if (dividerDrawByChild) {
                rectBound.left = rectBound.left + margin[0]
                rectBound.right = rectBound.right - margin[2]
            } else {
                if (parent.clipToPadding) {
                    rectBound.left = parent.paddingLeft + margin[0]
                    rectBound.right = parent.width - parent.paddingRight - margin[2]
                } else {
                    rectBound.left = 0 + margin[0]
                    rectBound.right = parent.width - margin[2]
                }
            }
            rectBound.left += view.translationX.toInt()
            rectBound.bottom += (view.translationY.toInt() - margin[3])
            rectBound.top = rectBound.bottom - getDrawableHeight(position, parent)

        } else {
            //横向
            if (dividerDrawByChild) {
                rectBound.top = rectBound.top + margin[1]
                rectBound.bottom = rectBound.bottom - margin[3]
            } else {
                if (parent.clipToPadding) {
                    rectBound.top = parent.paddingTop + margin[1]
                    rectBound.bottom = parent.height - parent.paddingBottom - margin[3]
                } else {
                    rectBound.top = 0 + margin[1]
                    rectBound.bottom = parent.height - margin[3]
                }
            }
            rectBound.top += view.translationY.toInt()
            rectBound.bottom += view.translationY.toInt()
            rectBound.right += (view.translationX.toInt() - margin[2])
            rectBound.left = rectBound.right - getDrawableHeight(position, parent)
        }
        return arrayListOf(rectBound)
    }

    class Builder(mContext: Context, layoutOrientation: Int) : BaseItemDecoration.Builder(mContext,layoutOrientation) {
        override fun build(): BaseItemDecoration {
            return LinerItemDecoration(this)
        }

    }
}