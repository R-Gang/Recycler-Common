package com.gang.recycler.kotlin.interfaces

import android.view.View

/**
 * Item 点击
 */
interface ViewOnItemClick {
    fun setOnItemClickListener(view: View?, position: Int)
}