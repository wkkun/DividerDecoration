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
    override fun setItemOffsets(
        position: Int,
        itemCount: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        if (span == 0) {
            if (parent.layoutManager !is GridLayoutManager) {
                Log.e(tag, "使用GridItemDecoration时,请确保是网格布局")
                outRect.set(0, 0, 0, 0)
                return
            }
            span = (parent.layoutManager as GridLayoutManager).spanCount
        }

        if (orientation == OrientationHelper.VERTICAL) {
            //纵向布局
            bottom = getDrawableHeight(position, parent) + margin[1] + margin[3]
            left = bottom / 2
            right = bottom / 2
            top = 0
            if (!isShowLastDivider && isLastRow(position, span, itemCount)) {
                bottom = 0
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
            if (position < span && isShowTopDivider) {
                //第一行
                top = if (topDividerWidth > 0) topDividerWidth else bottom
            }
            if (isLastRow(position, span, itemCount)) {
                if (!isShowLastDivider) {
                    bottom = 0
                } else if (bottomDividerWidth > 0) {
                    bottom = bottomDividerWidth
                }
            }
        } else {
            //横向布局
            //判断是否是最后一列
            right = getDrawableHeight(position, parent) + margin[0] + margin[2]
            left = 0
            bottom = right / 2
            top = right / 2

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
            if (position < span && isShowTopDivider) {
                //第一行
                left = if (topDividerWidth > 0) topDividerWidth else bottom
            }
            if (isLastRow(position, span, itemCount)) {
                if (!isShowLastDivider) {
                    right = 0
                } else if (bottomDividerWidth > 0) {
                    right = bottomDividerWidth
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
        if (span == 0)
            span = (parent.layoutManager as GridLayoutManager).spanCount
        if (orientation == OrientationHelper.VERTICAL) {
            //纵向
            if (isShowLastDivider || !isLastRow(position, span, itemCount)) {
                //绘制尾部的分割线
                left = viewBound.left + view.translationX.toInt()
                right = viewBound.right + view.translationX.toInt()
                bottom = viewBound.bottom + (view.translationY.toInt() - margin[3])
                top = bottom - dividerHeight
                rectLists.add(Rect(left, top, right, bottom))
            }
            top = viewBound.top + view.translationY.toInt()
            bottom = viewBound.bottom + view.translationY.toInt()
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
            //横向
            if (isShowLastDivider || !isLastRow(position, span, itemCount)) {
                //绘制尾部的分割线
                right = viewBound.right + (view.translationX.toInt() - margin[2])
                left = right - dividerHeight
                bottom = viewBound.bottom + view.translationY.toInt()
                top = viewBound.top + view.translationY.toInt()
                rectLists.add(Rect(left, top, right, bottom))
            }
            left = viewBound.left + view.translationX.toInt()
            right = viewBound.right + view.translationX.toInt()
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

        override fun build(): BaseItemDecoration {
            return GridItemDecoration(this)
        }

    }
}