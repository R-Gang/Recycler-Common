package com.gang.recycler.kotlin.interfaces

import android.view.View

/**
 * Item 长按
 */
interface ViewOnItemLongClick {
    fun setOnItemLongClickListener(view: View?, postion: Int)
}