package com.gang.recycler.kotlin.interfaces

import android.view.View

/**
 * page切换的监听器
 */
interface OnPageChangeListener {
    fun onPageSelected(itemPosition: Int, itemView: View?)
}