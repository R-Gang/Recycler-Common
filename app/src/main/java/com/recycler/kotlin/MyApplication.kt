package com.recycler.kotlin

import android.app.Application
import androidx.databinding.ktx.BuildConfig
import com.gang.recycler.kotlin.manager.LayoutManager
import com.gang.tools.kotlin.ToolsConfig
import com.gang.tools.kotlin.utils.initToolsUtils

/**
 * @ProjectName: Tools-Utils
 * @Package: com.okhttp.kotlin.base
 * @ClassName: MyApplication
 * @Description: java类作用描述
 * @Author: haoruigang
 * @CreateDate: 2022/3/7 16:30
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ToolsConfig.isShowLog = BuildConfig.DEBUG

        initToolsUtils(this)  // 初始化使用工具类
        LayoutManager.instance?.init(this) // 初始化RecyclerView
    }

}