package com.wkkun.decoration

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wkkun.divider.BaseItemDecoration
import com.wkkun.divider.GridItemDecoration
import com.wkkun.divider.ItemDecorationHelper
import com.wkkun.divider.LinerItemDecoration
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val type = intent.getIntExtra(MainActivity.TYPE, -1)
        recyclerView.adapter = ListAdapter(this)
        when (type) {
            MainActivity.TYPE_LINEAR -> {
                linear()
            }
            MainActivity.TYPE_GRID -> {
                grid()
            }
            MainActivity.TYPE_MULTI -> {
                multi()
            }
            MainActivity.TYPE_LINER_SPACE -> {
                space()
            }

            MainActivity.TYPE_GRID_SPACE -> {
                gridSpace()
            }
        }
    }


    private fun linear() {
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.HORIZONTAL)
                .setDividerWidthPx(20)//分割线的宽度 单位px
                .setDividerMarginPx(10, 10, 10, 10)//设置分割线距离item的间隔
                .setDividerDrawByChild(true)//设置绘制分割线的长度是否是根据item的长度来绘制 默认为false代表绘制是根据RecyclerView的长度来的
                .setIsDrawFirstItemTopDivider(true)
                .setRecyclerViewBottomSpacePx(100)
                .setRecyclerViewTopSpacePx(100)
                .setDividerColorProvider(object : BaseItemDecoration.DividerColorProvider {
                    override fun getDividerColor(position: Int, parent: RecyclerView): Int {
                        when ((position + 1) % 4) {
                            0 -> {
                                return Color.parseColor("#FF0000")
                            }
                            1 -> {
                                return Color.parseColor("#00FF00")
                            }
                            2 -> {
                                return Color.parseColor("#0000FF")
                            }
                            3 -> {
                                return Color.parseColor("#000000")
                            }
                            else -> {
                                return Color.parseColor("#000000")
                            }
                        }

                    }
                })//设置分割线绘制的颜色  我们可以设置在不同的位置绘制不同的颜色
                .build()
        )
    }


    private fun grid() {
        recyclerView.layoutManager = GridLayoutManager(this, 3, OrientationHelper.HORIZONTAL, false)
        recyclerView.addItemDecoration(
            GridItemDecoration.Builder(this, OrientationHelper.HORIZONTAL)
                .setDividerWidthPx(10)
                .setRecyclerViewTopSpacePx(100)
                .setRecyclerViewBottomSpacePx(100)
                .setDividerMarginPx(10, 10, 30, 30)
                .setDividerColorProvider(object : BaseItemDecoration.DividerColorProvider {
                    override fun getDividerColor(position: Int, parent: RecyclerView): Int {
                        return Color.parseColor("#FF0000")
                    }

                })
                .build()
        )
    }


    private fun multi() {
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)//分割线的宽度 单位px
                .setDividerMarginPx(10, 10, 10, 10)//设置分割线距离item的间隔
                .setDividerDrawByChild(false)//设置绘制分割线的长度是否是根据item的长度来绘制 默认为false代表绘制是根据RecyclerView的长度来的
                .setDividerPaintProvider(object : BaseItemDecoration.DividerPaintProvider {
                    override fun getDividerPaint(position: Int, parent: RecyclerView): Paint {
                        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                        paint.strokeWidth = 5f
                        paint.color = Color.BLUE
                        paint.style = Paint.Style.STROKE
                        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
                        return paint
                    }
                })
                .build()
        )
    }

    /**
     * 设置空格分割线
     */
    private fun space() {
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            ItemDecorationHelper
                .getSimpleLinerSpaceItemDecoration(
                    this,
                    OrientationHelper.VERTICAL, 100, 100, 20
                )
        )
    }


    /**
     * 设置网格空格分割线
     */
    private fun gridSpace() {
        recyclerView.layoutManager = GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            GridItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)
                .build()
        )
    }

    class ListAdapter(private val mContext: Context) : RecyclerView.Adapter<ListViewHolder>() {
        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_list, p0, false)
            )
        }

        override fun getItemCount(): Int {
            return 30
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
