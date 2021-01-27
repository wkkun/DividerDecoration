package com.wkkun.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DimenRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import java.util.*

/**
 *Date:2019/7/9
 *Author:kunkun.wang
 *Des:ItemDecoration的基类
 * 该分割线需要支持 网格 线性 瀑布布局
 * 分割线的种类 颜色 单纯的空格 drawable
 * 支持在不同的分割线位置绘制不同的视图
 **/
abstract class BaseItemDecoration constructor(build: Builder) : RecyclerView.ItemDecoration() {

    protected val tag: String = "BaseItemDecoration:"
    private val defaultColor = "#FFFFFF"

    private var paint: Paint = Paint()
    private var dividerWidth: Int = build.dividerWidth

    /**
     * 顶部的分割线高度 具有高优先级别
     */
    protected var topDividerWidth: Int = build.topDividerWidth

    /**
     * 底部的分割线高度 具有高优先级别
     */
    protected var bottomDividerWidth: Int = build.bottomDividerWidth

    protected val margin: IntArray = build.margin

    /**
     * 是否显示底部分割线
     */
    protected var isShowLastDivider: Boolean = build.isShowLastDivider

    /**
     * 是否显示顶部分割线
     */
    protected var isShowTopDivider: Boolean = build.isShowTopDivider

    private var dividerDrawableProvider: DividerDrawableProvider? = build.dividerDrawableProvider

    private var dividerColorProvider: DividerColorProvider? = build.dividerColorProvider

    private var dividerPaintProvider: DividerPaintProvider? = build.dividerPaintProvider

    protected var dividerSpaceProvider: DividerSpaceProvider? = build.dividerSpaceProvider

    private var dividerVisibleProvider: DividerVisibleProvider? = build.dividerVisibleProvider

    protected var orientation = build.orientation


    /**
     * 设置分割线绘制的长度是否依照子View的长度  默认是根据RecyclerView的长度或者高度进行绘制的
     */
    protected var dividerDrawByChild = build.dividerDrawByChild

    init {
        if (orientation != OrientationHelper.HORIZONTAL && orientation != OrientationHelper.VERTICAL) {
            throw RuntimeException("请先使用方法 Builder.setOrientation(orientation: Int),设置列表方向")
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //View的总数
        val itemSum = parent.adapter?.itemCount ?: parent.childCount
        //当前View的位置
        val position = parent.getChildAdapterPosition(view)
        setItemOffsets(position, itemSum, outRect, view, parent)
    }

    abstract fun setItemOffsets(
        position: Int,
        itemCount: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    )

    abstract fun getDrawRectBound(
        position: Int,
        itemCount: Int,
        view: View,
        parent: RecyclerView
    ): ArrayList<Rect>

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (dividerSpaceProvider != null) {
            //空格分割线 不绘制任何东西
            return
        }
        val childCount = parent.childCount
        //遍历所有的View 绘制分割线
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            //子View在adapter中的位置
            val childPosition = parent.getChildAdapterPosition(child)
            if ((parent.layoutManager !is GridLayoutManager) && dividerVisibleProvider?.shouldHideDivider(
                    childPosition,
                    parent
                ) == true
            ) {
                //隐藏分割线 网格不支持隐藏分割线
                continue
            }
            if (!isShowLastDivider && ((childPosition + 1) == parent.adapter?.itemCount ?: parent.childCount)) {
                //最后一个View 隐藏分割线
                if (parent.layoutManager !is GridLayoutManager)
                    continue
            }
            val rectBound = getDrawRectBound(
                childPosition,
                parent.adapter?.itemCount ?: parent.childCount, child, parent
            )
            if (dividerColorProvider != null) {
                val color = dividerColorProvider?.getDividerColor(childPosition, parent)
                    ?: Color.parseColor(defaultColor)
                paint.color = color
                paint.strokeWidth = dividerWidth.toFloat()
                rectBound.forEach {
                    c.drawRect(
                        it,
                        paint
                    )

                }
                continue
            }
            if (dividerPaintProvider != null) {
                paint = dividerPaintProvider?.getDividerPaint(childPosition, parent) ?: Paint()
                rectBound.forEach {
                    if (Math.abs(it.left - it.right) > Math.abs(it.top - it.bottom)) {
//                        横条
                        c.drawLine(
                            it.left.toFloat(),
                            (it.top.toFloat() + it.bottom.toFloat()) / 2,
                            it.right.toFloat(),
                            (it.top.toFloat() + it.bottom.toFloat()) / 2, paint
                        )
                    } else {
//                        竖条
                        c.drawLine(
                            (it.left.toFloat() + it.right.toFloat()) / 2,
                            it.top.toFloat(),
                            (it.left.toFloat() + it.right.toFloat()) / 2,
                            it.bottom.toFloat(), paint
                        )
                    }
                }
                continue
            }
            if (dividerDrawableProvider != null) {
                val drawable = dividerDrawableProvider?.getDividerDraw(childPosition, parent)
                if (drawable != null) {
                    rectBound.forEach {
                        drawable.bounds = it
                        drawable.draw(c)
                    }
                }
                continue
            }

        }
    }


    abstract class Builder(private val mContext: Context, layoutOrientation: Int) {
        internal var orientation: Int = layoutOrientation
        internal var dividerWidth: Int = 9

        internal var topDividerWidth: Int = -1
        internal var bottomDividerWidth: Int = -1

        internal val margin: IntArray = intArrayOf(0, 0, 0, 0)

        internal var isShowLastDivider: Boolean = false

        internal var isShowTopDivider: Boolean = false

        internal var dividerDrawByChild: Boolean = false


        internal var dividerDrawableProvider: DividerDrawableProvider? = null

        internal var dividerColorProvider: DividerColorProvider? = null

        internal var dividerPaintProvider: DividerPaintProvider? = null

        internal var dividerSpaceProvider: DividerSpaceProvider? = null

        internal var dividerVisibleProvider: DividerVisibleProvider? = null

        /**
         * 设置分割线的高度
         */
        fun setDividerWidth(@DimenRes dividerWidth: Int): Builder {
            this.dividerWidth = mContext.resources.getDimension(dividerWidth).toInt()
            return this
        }

        /**
         * 设置分割线的高度
         */
        fun setDividerWidthPx(dividerWidth: Int): Builder {
            this.dividerWidth = dividerWidth
            return this
        }

        /**
         * 设置分割线的高度
         */
        fun setTopDividerWidthPx(dividerWidth: Int): Builder {
            this.topDividerWidth = dividerWidth
            return this
        }


        /**
         * 设置分割线的高度
         */
        fun setBottomDividerWidthPx(dividerWidth: Int): Builder {
            this.bottomDividerWidth = dividerWidth
            return this
        }

        /**
         * 设置分割线的 周边Margin
         * @param leftMargin 左边距
         * @param topMargin  上边距
         * @param rightMargin 右边距
         * @param bottomMargin 下边距
         */
        fun setDividerMargin(
            @DimenRes leftMargin: Int,
            @DimenRes topMargin: Int,
            @DimenRes rightMargin: Int,
            @DimenRes bottomMargin: Int
        ): Builder {
            if (mContext.resources == null) {
                return this
            }
            return setDividerMarginPx(
                mContext.resources.getDimension(leftMargin).toInt(),
                mContext.resources.getDimension(topMargin).toInt(),
                mContext.resources.getDimension(rightMargin).toInt(),
                mContext.resources.getDimension(bottomMargin).toInt()
            )
        }

        fun setDividerMarginPx(
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int
        ): Builder {
            margin[0] = leftMargin
            margin[1] = topMargin
            margin[2] = rightMargin
            margin[3] = bottomMargin
            return this
        }

        fun setDividerDrawableProvider(dividerDrawableProvider: DividerDrawableProvider): Builder {
            this.dividerDrawableProvider = dividerDrawableProvider
            return this
        }

        fun setDividerColorProvider(dividerColorProvider: DividerColorProvider): Builder {
            this.dividerColorProvider = dividerColorProvider
            return this
        }

        fun setDividerPaintProvider(dividerPaintProvider: DividerPaintProvider): Builder {
            this.dividerPaintProvider = dividerPaintProvider
            return this
        }

        /**
         * 设置空格分隔线 以及大小  但是不支持网格分割线
         */
        fun setDividerSpaceProvider(dividerSpaceProvider: DividerSpaceProvider): Builder {
            this.dividerSpaceProvider = dividerSpaceProvider
            return this
        }

        /**
         * 设置隐藏某些分割线
         * 但是需要注意的是 网格分割线不支持该形式
         */
        fun setDividerVisibleProvider(dividerVisibleProvider: DividerVisibleProvider): Builder {
            this.dividerVisibleProvider = dividerVisibleProvider
            return this
        }

        fun setDividerDrawByChild(drawByChild: Boolean): Builder {
            dividerDrawByChild = drawByChild
            return this
        }

        /**
         * 是否显示尾部的分割线
         * 对于线性分割线是最后一个条目是否有分割线
         * 对于网格布局就是最后一列或者是最后一行是否有分割线
         * @param showLastDivider true显示尾部分割线  false 不显示尾部分割线 默认是不显示
         */
        fun showLastDivider(showLastDivider: Boolean = false): Builder {
            isShowLastDivider = showLastDivider
            return this
        }


        /**
         * 是否显示顶部分割线
         */
        fun showTopDivider(isShowTopDivider: Boolean = false): Builder {
            this.isShowTopDivider = isShowTopDivider
            return this
        }

        abstract fun build(): BaseItemDecoration

    }

    interface DividerDrawableProvider {
        fun getDividerDraw(position: Int, parent: RecyclerView): Drawable
    }

    interface DividerColorProvider {
        fun getDividerColor(position: Int, parent: RecyclerView): Int
    }

    interface DividerPaintProvider {
        fun getDividerPaint(position: Int, parent: RecyclerView): Paint
    }

    /**
     * 空格分割线 此分割线可以设置不同的分割线的高度 但是该分割线只能适用于 线性分割线 网格分割线请用 dividerWidth属性设置 间隔
     */
    interface DividerSpaceProvider {
        fun getDividerSpace(position: Int, parent: RecyclerView): Int
    }


    interface DividerVisibleProvider {
        fun shouldHideDivider(position: Int, parent: RecyclerView): Boolean
    }

    /**
     * 获取需要绘制的分割线的高度
     */
    protected fun getDrawableHeight(position: Int, parent: RecyclerView): Int {
        if (dividerDrawableProvider != null) {
            dividerDrawableProvider?.getDividerDraw(position, parent)?.apply {
                return if (orientation == OrientationHelper.HORIZONTAL) {
                    this.intrinsicWidth
                } else {
                    this.intrinsicHeight
                }
            }
        }
        return dividerWidth
    }

}