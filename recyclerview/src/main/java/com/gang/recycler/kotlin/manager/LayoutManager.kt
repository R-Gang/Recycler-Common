package com.gang.recycler.kotlin.manager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Date: 2019/4/10
 * Author: haoruigang
 * Description: recyclerview 管理
 */
class LayoutManager {

    private var mContext: Context? = null

    fun init(context: Context?): LayoutManager {
        mContext = context
        return this
    }


    fun initRecyclerView(
        recyclerView: RecyclerView,
        isVertical: Boolean = true
    ): LinearLayoutManager {
        val manager = LinearLayoutManager(mContext)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    fun initRecyclerPager(
        recyclerView: RecyclerView,
        isVertical: Boolean = true
    ): PagerLayoutManager {
        val manager = PagerLayoutManager(mContext)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    fun initRecyclerGrid(
        recyclerView: RecyclerView,
        span: Int,
        isVertical: Boolean = true,
    ): GridLayoutManager {
        val manager = GridLayoutManager(mContext, span)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    fun initRecyclerFullyGrid(
        recyclerView: RecyclerView,
        span: Int,
        isVertical: Boolean = true,
    ): FullyGridLayoutManager {
        val manager = FullyGridLayoutManager(mContext, span)
        manager.orientation =
            if (isVertical) RecyclerView.VERTICAL else LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        return manager
    }

    /**
     * 流式布局recyclerView
     */
    fun initFlexbox(recyclerView: RecyclerView, isDefault: Boolean = true): FlexboxLayoutManager {
        val layoutManager = FlexboxLayoutManager(mContext)
        if (isDefault) {
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.flexWrap = FlexWrap.WRAP
            recyclerView.layoutManager = layoutManager
        }
        return layoutManager
    }

    companion object {

        private var mInstance: LayoutManager? = null

        /**
         * 获取LayoutManager实例
         */
        @get:Synchronized
        val instance: LayoutManager?
            get() {
                if (null == mInstance) {
                    mInstance =
                        LayoutManager()
                }
                return mInstance
            }
    }
}