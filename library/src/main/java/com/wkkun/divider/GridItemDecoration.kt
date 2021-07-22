package com.wkkun.divider

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import java.util.*

/**
 *Date:2019/7/9
 *Author:kunkun.wang
 *Des:网格布局分割线
 **/
class GridItemDecoration(builder: Builder) : BaseItemDecoration(builder) {
    private var left = 0
    private var top = 0
    private var right = 0
    private var bottom = 0
    private var span: Int = 0
    private var dividerHeight = 0
    private var ignoreHeadLine = 0
    private var ignoreFooterLine = 0
    var isDrawLastDivider: Boolean = builder.isDrawLastDivider

    override fun setItemOffsets(
        position: Int,
        itemCount: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        dividerHeight = getDrawableHeight(position, parent)
        if (span == 0) {
            if (parent.layoutManager !is GridLayoutManager) {
                Log.e(tag, "使用GridItemDecoration时,请确保是网格布局")
                outRect.set(0, 0, 0, 0)
                return
            }
            span = (parent.layoutManager as GridLayoutManager).spanCount
            ignoreHeadLine = (ignoreHeadItemCount / span.toFloat() + 0.5f).toInt()
            ignoreFooterLine = (ignoreFooterItemCount / span.toFloat() + 0.5f).toInt()
        }

        if (orientation == OrientationHelper.VERTICAL) {
            //纵向布局
            bottom = dividerHeight + margin[1] + margin[3]
            left = dividerHeight / 2 + margin[2]
            right = dividerHeight / 2 + margin[0]
            top = 0
            if (position < span) {
                //第一行
                top = recyclerViewTopSpace
                if (isDrawFirstTopDivider) {
                    top += bottom
                }
            }
            when ((position + 1) % span) {
                0 -> {
                    //最后一列
                    right = 0
                }
                1 -> {
                    //第一列
                    left = 0
                }
            }
            if (isLastRow(position, span, itemCount)) {
                bottom += recyclerViewBottomSpace
                if (!isDrawLastDivider) {
                    bottom = recyclerViewBottomSpace
                }
            }

        } else {
            //横向布局
            bottom = dividerHeight / 2 + margin[0]
            top = dividerHeight / 2 + margin[2]
            right = dividerHeight + margin[1] + margin[3]
            left = 0
            if (position < span) {
                //第一行
                left = recyclerViewTopSpace
                if (isDrawFirstTopDivider) {
                    left += bottom
                }
            }
            if (isLastRow(position, span, itemCount)) {
                right += recyclerViewBottomSpace
                if (!isDrawLastDivider) {
                    right = recyclerViewBottomSpace
                }
            }
            when ((position + 1) % span) {
                0 -> {
                    //最后一行
                    bottom = 0
                }
                1 -> {
                    //第一行
                    top = 0
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
        if (parent.layoutManager !is GridLayoutManager) {
            Log.e(tag, "使用GridItemDecoration时,请确保是网格布局")
            return arrayListOf()
        }
        val rectLists = ArrayList<Rect>()
        val viewBound = Rect()
        val dividerHeight = getDrawableHeight(position, parent)
        parent.getDecoratedBoundsWithMargins(view, viewBound)
        if (span == 0) {
            span = (parent.layoutManager as GridLayoutManager).spanCount
        }
        if (orientation == OrientationHelper.VERTICAL) {
            val isLastRow = isLastRow(position, span, itemCount)
            //纵向
            //添加底部分割线
            left = viewBound.left + view.translationX.toInt()
            right = viewBound.right + view.translationX.toInt()
            bottom = viewBound.bottom + (view.translationY.toInt() - margin[3])
            if (isLastRow) {
                bottom -= recyclerViewBottomSpace
            }
            top = bottom - dividerHeight
            if (!isLastRow || isDrawLastDivider)
                rectLists.add(Rect(left, top, right, bottom))

            if (isDrawFirstTopDivider && position < span) {
                //添加顶部分割线
                top = viewBound.top + view.translationY.toInt() + recyclerViewTopSpace + margin[1]
                bottom = top + dividerHeight
                rectLists.add(Rect(left, top, right, bottom))
            }
            top = viewBound.top + view.translationY.toInt()
            if (position < span) {
                top += recyclerViewTopSpace
            }
            bottom = viewBound.bottom
            if (isLastRow) {
                bottom -= recyclerViewBottomSpace
                if (isDrawLastDivider)
                    bottom -= (margin[3] + dividerHeight)
            }
            when ((position + 1) % span) {
                0 -> {
                    //最后一列
                    //绘制左侧的分割线
                    left = viewBound.left + view.translationX.toInt()
                    right = left + dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))
                }
                1 -> {
                    //第一列
                    //绘制右侧的分割线
                    right = viewBound.right + view.translationX.toInt()
                    left = right - dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))
                }
                else -> {
                    //其他部分
                    //绘制左右两侧的分割线
                    //左侧
                    left = viewBound.left + view.translationX.toInt()
                    right = left + dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))
                    //右侧
                    right = viewBound.right + view.translationX.toInt()
                    left = right - dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))

                }
            }
        } else {
            val isLastRow = isLastRow(position, span, itemCount)
            //横向
            //绘制尾部的分割线
            bottom = viewBound.bottom + view.translationY.toInt()
            top = viewBound.top + view.translationY.toInt()
            right = viewBound.right + view.translationX.toInt() - margin[3]
            if (isLastRow) {
                right -= recyclerViewBottomSpace
            }
            left = right - dividerHeight
            if (!isLastRow || isDrawLastDivider)
                rectLists.add(Rect(left, top, right, bottom))
            if (isDrawFirstTopDivider && position < span) {
                //添加顶部分割线
                bottom = viewBound.bottom + view.translationY.toInt()
                top = viewBound.top + view.translationY.toInt()
                left = viewBound.left + margin[1] + recyclerViewTopSpace
                right = left + dividerHeight
                rectLists.add(Rect(left, top, right, bottom))
            }
            left = viewBound.left
            right = viewBound.right
            if (position < span) {
                left += recyclerViewTopSpace
            }
            if (isLastRow) {
                right -= recyclerViewBottomSpace
                if (isDrawLastDivider)
                    right -= (dividerHeight + (margin[3]))
            }
            when ((position + 1) % span) {
                1 -> {
                    //第一行
                    //绘制下方分割线
                    bottom = viewBound.bottom + view.translationY.toInt()
                    top = bottom - dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))
                }
                0 -> {
                    //最后一行
                    //绘制上方分割线
                    top = viewBound.top + view.translationY.toInt()
                    bottom = top + dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))
                }
                else -> {
                    //中间行
                    //绘制上下两方分割线
                    top = viewBound.top + view.translationY.toInt()
                    bottom = top + dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))

                    bottom = viewBound.bottom + view.translationY.toInt()
                    top = bottom - dividerHeight / 2
                    rectLists.add(Rect(left, top, right, bottom))
                }
            }
        }
        return rectLists
    }

    /**
     * 是否是最后一行
     */
    private fun isLastRow(position: Int, spanCount: Int, itemCount: Int): Boolean {
        var remind = itemCount % spanCount
        if (remind == 0) {
            remind = spanCount
        }
        return (itemCount - position) <= remind
    }

    class Builder(mContext: Context, layoutOrientation: Int) :
        BaseItemDecoration.Builder(mContext, layoutOrientation) {

        var isDrawLastDivider: Boolean = false
        override fun build(): BaseItemDecoration {
            return GridItemDecoration(this)
        }

        fun setIsDrawLastDivider(isDrawLastDivider: Boolean): Builder {
            this.isDrawLastDivider = isDrawLastDivider
            return this
        }

    }
}