package com.gang.recycler.kotlin.manager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * author：haoruigang
 * 选择附件
 * data：2018-4-10 17:12:32
 */
class FullyGridLayoutManager : GridLayoutManager {
    constructor(context: Context?, spanCount: Int) : super(context, spanCount) {}
    constructor(
        context: Context?,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
    ) : super(context, spanCount, orientation, reverseLayout) {
    }

    private val mMeasuredDimension = IntArray(2)
    override fun onMeasure(
        recycler: Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int,
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)
        var width = 0
        var height = 0
        val count = itemCount
        val span = spanCount
        for (i in 0 until count) {
            measureScrapChild(
                recycler, i,
                View.MeasureSpec.makeMeasureSpec(
                    i,
                    View.MeasureSpec.UNSPECIFIED
                ),
                View.MeasureSpec.makeMeasureSpec(
                    i,
                    View.MeasureSpec.UNSPECIFIED
                ),
                mMeasuredDimension
            )
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (i % span == 0) {
                    width = width + mMeasuredDimension[0]
                }
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                if (i % span == 0) {
                    height = height + mMeasuredDimension[1]
                }
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> width = widthSize
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
            }
        }
        when (heightMode) {
            View.MeasureSpec.EXACTLY -> height = heightSize
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
            }
        }
        setMeasuredDimension(width, height)
    }

    val mState = RecyclerView.State()
    private fun measureScrapChild(
        recycler: Recycler, position: Int, widthSpec: Int,
        heightSpec: Int, measuredDimension: IntArray,
    ) {
        val itemCount = mState.itemCount
        if (position < itemCount) {
            try {
                val view = recycler.getViewForPosition(0)
                if (view != null) {
                    val p =
                        view.layoutParams as RecyclerView.LayoutParams
                    val childWidthSpec = ViewGroup.getChildMeasureSpec(
                        widthSpec,
                        paddingLeft + paddingRight, p.width
                    )
                    val childHeightSpec = ViewGroup.getChildMeasureSpec(
                        heightSpec,
                        paddingTop + paddingBottom, p.height
                    )
                    view.measure(childWidthSpec, childHeightSpec)
                    measuredDimension[0] =
                        view.measuredWidth + p.leftMargin + p.rightMargin
                    measuredDimension[1] =
                        view.measuredHeight + p.bottomMargin + p.topMargin
                    recycler.recycleView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}