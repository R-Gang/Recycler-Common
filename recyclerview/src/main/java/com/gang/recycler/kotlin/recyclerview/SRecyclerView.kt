package com.gang.recycler.kotlin.recyclerview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gang.recycler.kotlin.manager.LayoutManager
import kotlin.math.abs

/**
 * 上下滑动切换控件
 */
class SRecyclerView : FrameLayout {

    private var recycView: RecyclerView? = null
    private var tvTip: TextView? = null
    private var layoutManager: LinearLayoutManager? = null

    private var topTip: String? = null
    private var bottomTip: String? = null

    private var mDownX = -1f
    private var mDownY = -1f
    private var mLastY = -1f
    private var isPulling = false

    constructor(context: Context) : super(context) {
        init(true)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(true)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(true)
    }

    fun init(isVertical: Boolean) {
        setBackgroundColor(COLOR_BG)
        // 提示TextView
        tvTip = TextView(context)
        tvTip?.textSize = TEXT_SIZE
        tvTip?.setTextColor(TEXT_COLOR)
        tvTip?.gravity = Gravity.CENTER_HORIZONTAL
        addView(tvTip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        // RecyclerView
        recycView = RecyclerView(context)
        addView(recycView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recycView?.let { layoutManager = LayoutManager().initRecyclerView(it, isVertical) }
        recycView?.layoutManager = layoutManager
    }

    fun <T : RecyclerView.ViewHolder> setAdapter(adapter: RecyclerView.Adapter<T>) {
        recycView?.adapter = adapter
    }

    fun getRecyclerView(): RecyclerView? {
        return this.recycView
    }

    fun getLayoutManager(): LinearLayoutManager? {
        return this.layoutManager
    }

    /**
     * 不带动画
     */
    fun scrollToPosition(position: Int) {
        layoutManager?.scrollToPositionWithOffset(position, 0)
    }

    /**
     * 不带动画(这个方法是自己去控制移动的距离，单位应该是像素。)
     */
    fun scrollToBy(x: Int = 0, y: Int) {
        recycView?.scrollBy(x, y)
    }

    /**
     * 带动画
     */
    fun smoothScrollToPosition(position: Int, state: RecyclerView.State) {
        layoutManager?.smoothScrollToPosition(recycView, state, position)
    }

    /**
     * 滑到顶部文字
     */
    fun scrollTopText(textCon: String? = "没有更多作品啦") {
        topTip = textCon
    }

    /**
     * 滑到底部文字
     */
    fun scrollBottomText(textCon: String? = "已经到底啦") {
        bottomTip = textCon
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.rawX
                mDownY = ev.rawY
                mLastY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.rawX - mDownX
                val deltaY = ev.rawY - mDownY
                if (abs(deltaY) > abs(deltaX)) {
                    recycView?.apply {
                        if (deltaY > 0 && !canScrollVertically(-1)) {
                            return true
                        } else if (deltaY < 0 && !canScrollVertically(1)) {
                            return true
                        }
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (mDownY == -1f) {
            mDownY = ev.rawY
        }
        if (mLastY == -1f) {
            mLastY = ev.rawY
        }
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                recycView?.let {
                    val deltaY = ev.rawY - mDownY
                    if (deltaY > 0 && !it.canScrollVertically(-1)) {
                        tvTip?.text = topTip
                        tvTip?.y = deltaY / DRAG_RATE - TEXT_MARGIN
                        it.y = deltaY / DRAG_RATE
                        isPulling = true
                    } else if (deltaY < 0 && !it.canScrollVertically(1)) {
                        tvTip?.text = bottomTip
                        tvTip?.y = height + deltaY / DRAG_RATE + TEXT_MARGIN
                        it.y = deltaY / DRAG_RATE
                        isPulling = true
                    }
                    mLastY = ev.rawY
                }
            }
            else -> {
                mDownY = -1f
                mLastY = -1f
                if (isPulling) {
                    recycView?.let {
                        // TextView 归位
                        val animation: TranslateAnimation = if (it.y > 0) {
                            TranslateAnimation(0F, 0F, tvTip!!.y, -TEXT_MARGIN - tvTip!!.height)
                        } else {
                            TranslateAnimation(0F, 0F, tvTip!!.y, height + TEXT_MARGIN)
                        }
                        animation.duration = 300
                        animation.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {
                                tvTip?.y = -TEXT_MARGIN - tvTip?.height!!
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                        tvTip?.y = 0f
                        tvTip?.startAnimation(animation)
                        // RecyclerView 归位
                        val animation1 = TranslateAnimation(0F, 0F, it.y, 0F)
                        animation1.duration = 300
                        animation1.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {
                                it.y = 0f
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                        it.y = 0f
                        it.startAnimation(animation1)
                        isPulling = false
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    companion object {
        val COLOR_BG = Color.TRANSPARENT // 背景色
        val DRAG_RATE = 2.5f // 下拉上拉的粘性（数值越大越难下拉）
        val TEXT_COLOR = -0x666667 // 提示文字颜色
        val TEXT_SIZE = 12f // 提示文字大小
        val TEXT_MARGIN = 150f // 提示文字和 RecyclerView 的间距

    }
}