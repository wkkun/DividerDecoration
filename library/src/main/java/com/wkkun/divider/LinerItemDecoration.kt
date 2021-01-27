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

    private var left = 0
    private var right = 0
    private var top = 0
    private var bottom = 0
    private var decorationHeight = 0
    override fun setItemOffsets(
        position: Int,
        itemCount: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        if (orientation == OrientationHelper.VERTICAL) {
            //纵向
            decorationHeight = dividerSpaceProvider?.getDividerSpace(position, parent)
                ?: getDrawableHeight(position, parent)
            left = 0
            top = 0
            right = 0
            bottom = decorationHeight + margin[1] + margin[3]
            if (position == 0 && isShowTopDivider) {
                top = if (topDividerWidth > 0) topDividerWidth else decorationHeight
                top += margin[3]
            }
            if (position == (itemCount - 1)) {
                if (!isShowLastDivider) {
                    bottom = 0
                } else if (bottomDividerWidth > 0) {
                    bottom = bottomDividerWidth + margin[3]
                }
            }

        } else {
            //横向
            decorationHeight = dividerSpaceProvider?.getDividerSpace(position, parent)
                ?: getDrawableHeight(position, parent)
            left = 0
            top = 0
            right = decorationHeight + margin[0] + margin[2]
            bottom = 0
            if (position == 0 && isShowTopDivider) {
                left = if (topDividerWidth > 0) topDividerWidth else decorationHeight
                left += margin[2]
            }

            if (position == (itemCount - 1)) {
                if (!isShowLastDivider) {
                    right = 0
                } else if (bottomDividerWidth > 0) {
                    right = bottomDividerWidth + margin[0] + margin[2]
                }
            }
        }
        outRect.set(left, top, right, bottom)
    }

    override fun getDrawRectBound(
        position: Int,
        itemCount: Int,
        view: View,
        parent: RecyclerView
    ): ArrayList<Rect> {
        val rectBound = Rect()
        val list = ArrayList<Rect>()
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
            if (position == (itemCount - 1) && isShowLastDivider) {
                rectBound.bottom += view.translationY.toInt()
                rectBound.top = if (bottomDividerWidth > 0) {
                    rectBound.bottom - bottomDividerWidth
                } else {
                    rectBound.bottom - getDrawableHeight(position, parent)
                }
            } else {
                rectBound.bottom += (view.translationY.toInt() - margin[3])
                rectBound.top = rectBound.bottom - getDrawableHeight(position, parent)
            }

            list.add(rectBound)

            if (position == 0 && isShowTopDivider) {
                val rectTop = Rect()
                parent.getDecoratedBoundsWithMargins(view, rectTop)
                rectTop.left = rectBound.left
                rectTop.right = rectBound.right
                rectTop.top += view.translationY.toInt()
                rectTop.bottom = if (topDividerWidth > 0) {
                    rectTop.top + topDividerWidth
                } else {
                    rectTop.top + getDrawableHeight(position, parent)
                }
                list.add(rectTop)
            }

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
            if (position == (itemCount - 1) && isShowLastDivider) {
                rectBound.right += view.translationX.toInt()
                rectBound.left = if (bottomDividerWidth > 0) {
                    rectBound.right - bottomDividerWidth
                } else {
                    rectBound.right - getDrawableHeight(position, parent)
                }
            } else {
                rectBound.right += (view.translationX.toInt() - margin[2])
                rectBound.left = rectBound.right - getDrawableHeight(position, parent)
            }
            list.add(rectBound)

            if (position == 0 && isShowTopDivider) {
                val rectTop = Rect()
                parent.getDecoratedBoundsWithMargins(view, rectTop)
                rectTop.top = rectBound.top
                rectTop.bottom = rectBound.bottom
                rectTop.left += view.translationY.toInt()
                rectTop.right = if (topDividerWidth > 0) {
                    rectTop.left + topDividerWidth
                } else {
                    rectTop.left + getDrawableHeight(position, parent)
                }
                list.add(rectTop)
            }

        }
        return list
    }

    class Builder(mContext: Context, layoutOrientation: Int) :
        BaseItemDecoration.Builder(mContext, layoutOrientation) {
        override fun build(): BaseItemDecoration {
            return LinerItemDecoration(this)
        }

    }
}