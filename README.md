# DividerDecoration

## 简述: ##

RecyclerView分割线,
![](https://github.com/wkkun/DividerDecoration/blob/master/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20190715174937_gaitubao_465x967.jpg)


![](https://github.com/wkkun/DividerDecoration/blob/master/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20190715174941_gaitubao_465x967.jpg)

![](https://github.com/wkkun/DividerDecoration/blob/master/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20190715174945_gaitubao_465x967.jpg)

![](https://github.com/wkkun/DividerDecoration/blob/master/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20190715174949_gaitubao_465x967.jpg)



## 功能: ##

1. 支持设置分割线宽度,左右上下的margin
2. 支持绘制分割线的长度是依据整体recyclerView还是依据item
3. 支持设置自定义绘制分割线 
4. 支持设置不同位置的分割线的显示隐藏 
5. 支持线性分割线
6. 支持网格分割线


## 使用: ##

一,线性分割线

可以绘制的分割线
   

```java
recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)//分割线的宽度 单位px
                .setDividerMarginPx(10, 10, 10, 10)//设置分割线距离item的间隔
                .setDividerDrawByChild(true)//设置绘制分割线的长度是否是根据item的长度来绘制 默认为false代表绘制是根据RecyclerView的长度来的
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
```

   
空格分割线

```java
recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)
                .showLastDivider(true)
                .showTopDivider(true)
                .setBottomDividerWidthPx(50)
                .setTopDividerWidthPx(50)
                .build()
        )
```

二、网格分割线

```java
recyclerView.addItemDecoration(
            GridItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(10)
                .setDividerMarginPx(10, 10, 10, 10)
                .setDividerColorProvider(object : BaseItemDecoration.DividerColorProvider {
                    override fun getDividerColor(position: Int, parent: RecyclerView): Int {
                        return Color.parseColor("#FF0000")
                    }

                })
                .build()
        )
```

  /**
     * 设置网格空格分割线
     */

```java
recyclerView.addItemDecoration(
            GridItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)
                .showTopDivider(true)
                .setTopDividerWidthPx(50)
                .setBottomDividerWidthPx(50)
                .showLastDivider(true)
                .build()
        )
```


项目地址:[点击我,带走我 ^_^](https://github.com/wkkun/DividerDecoration)