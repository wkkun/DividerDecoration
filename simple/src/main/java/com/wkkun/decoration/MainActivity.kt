package com.wkkun.decoration

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val TYPE_LINEAR = 1
        const val TYPE_GRID = 2
        const val TYPE_MULTI = 3
        const val TYPE_SPACE = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, ListActivity::class.java)

        bt1.setOnClickListener {
            //线性
            intent.putExtra(TYPE, TYPE_LINEAR)
            startActivity(intent)
        }

        bt2.setOnClickListener {
            //网格
            intent.putExtra(TYPE, TYPE_GRID)
            startActivity(intent)
        }

        bt3.setOnClickListener {
            //混合
            intent.putExtra(TYPE, TYPE_MULTI)
            startActivity(intent)
        }

        bt4.setOnClickListener {
            //空格
            intent.putExtra(TYPE, TYPE_SPACE)
            startActivity(intent)
        }


    }
}
