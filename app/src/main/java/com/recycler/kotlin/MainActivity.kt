package com.recycler.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.utils.toastCustom
import com.recycler.kotlin.databinding.ActivityMainBinding
import com.recycler.kotlin.demo.HomeIcon
import com.recycler.kotlin.demo.HomeMenuAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private var homeMenu = arrayListOf<HomeIcon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 原始方式
        // setContentView(R.layout.activity_main)

        /*
        方式1 视图绑定
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        */

        /*
        方式2 数据绑定
        */
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        mainBinding.mainData = this

        homeMenu.add(HomeIcon(0, R.mipmap.cate1, "企业简介"))
        homeMenu.add(HomeIcon(1, R.mipmap.cate2, "业务板块"))
        homeMenu.add(HomeIcon(2, R.mipmap.cate3, "产品中心"))
        homeMenu.add(HomeIcon(3, R.mipmap.cate4, "营销网络"))
        LayoutManager.instance?.initRecyclerGrid(mainBinding.recyclerView, 4)
        mainBinding.recyclerView.adapter = HomeMenuAdapter(
            homeMenu,
            this,
            R.layout.item_home_menu
        )

    }

    fun Custom() {
        toastCustom("Tools-Utils")
    }

}