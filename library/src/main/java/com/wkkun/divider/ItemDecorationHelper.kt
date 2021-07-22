package com.wkkun.divider

import android.content.Context
import android.support.v7.widget.RecyclerView

object ItemDecorationHelper {

    fun getSimpleLinerSpaceItemDecoration(context: Context,layoutOrientation:Int,headSpace:Int,footerSpace:Int,itemSpace:Int): RecyclerView.ItemDecoration{
        return LinerItemDecoration
            .Builder(context,layoutOrientation)
            .setRecyclerViewTopSpacePx(headSpace)
            .setRecyclerViewBottomSpacePx(footerSpace)
            .setDividerWidthPx(itemSpace)
            .setIgnoreFootItemCount(1)
            .build()
    }
}