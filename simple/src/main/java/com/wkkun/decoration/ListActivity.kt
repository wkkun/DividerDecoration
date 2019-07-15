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
            MainActivity.TYPE_SPACE -> {
                space()
            }
        }
    }


    private fun linear() {
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)//分割线的宽度 单位px
                .setDividerMarginPx(10, 10, 10, 20)//设置分割线距离item的间隔
                .setDividerDrawByChild(true)//设置绘制分割线的长度是否是根据item的长度来绘制 默认为false代表绘制是根据RecyclerView的长度来的
                .setHeadViewCount(0)//设置头布局的个数 默认为0 头布局之间没有分割线 以及头布局与第一条数据之间也是没有分割线
                .setFooterViewCount(0)//设置尾布局的个数 默认为0 尾布局之间没有分割线
                .showLastDivider(false)//最后一个item后面是否有分割线 默认为false
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
                .setDividerVisibleProvider(object : BaseItemDecoration.DividerVisibleProvider {
                    override fun shouldHideDivider(position: Int, parent: RecyclerView): Boolean {
                        //在3的倍数位置 不显示颜色
                        return (position + 1) % 3 == 0
                    }
                })//设置在某个位置隐藏分割线 但是分割线的间隔还是在的,只是不再绘制而已
                .build()
        )
    }


    private fun grid() {
        recyclerView.layoutManager = GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            GridItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(10)
                .setFooterViewCount(0)
                .setHeadViewCount(0)
                .setDividerMarginPx(10, 10, 10, 10)
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
                .setHeadViewCount(0)//设置头布局的个数 默认为0 头布局之间没有分割线 以及头布局与第一条数据之间也是没有分割线
                .setFooterViewCount(0)//设置尾布局的个数 默认为0 尾布局之间没有分割线
                .showLastDivider(false)//最后一个item后面是否有分割线 默认为false
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

    private fun space() {
        recyclerView.layoutManager = LinearLayoutManager(this,  OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(10)
                .setFooterViewCount(0)
                .setHeadViewCount(0)
                .setDividerMarginPx(10, 10, 10, 10)
                .setDividerSpaceProvider(object :BaseItemDecoration.DividerSpaceProvider{
                    override fun getDividerSpace(position: Int, parent: RecyclerView): Int {
                        return 20+position
                    }

                })
                .build()
        )
    }

    class ListAdapter(private val mContext: Context) : RecyclerView.Adapter<ListViewHolder>() {
        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list, p0, false))
        }

        override fun getItemCount(): Int {
            return 30
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
