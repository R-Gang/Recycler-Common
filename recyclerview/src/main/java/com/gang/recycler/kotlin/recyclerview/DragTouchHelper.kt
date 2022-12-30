package com.gang.recycler.kotlin.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @CreateDate:     2022/12/5 13:16
 * @Author:         haoruigang
 * @ClassName:      DragTouchHelper
 * @Description:    RecyclerView 拖拽、侧滑、手指松开
 */
open class DragTouchHelper(
    val callback: TouchCallback,
) :
    ItemTouchHelper(callback) {

    // 拖拽开关
    fun setEnableDrag(enableDrag: Boolean) {
        callback.setEnableDrag(enableDrag)
    }

    // 侧滑开关
    fun setEnableSwipe(enableSwipe: Boolean) {
        callback.setEnableSwipe(enableSwipe)
    }

}

class TouchCallback(callbackListener: OnItemTouchCallbackListener?) :
    ItemTouchHelper.Callback() {
    private var isEnableSwipe //允许滑动
            = false
    private var isEnableDrag //允许拖动
            = false
    private val callbackListener //回调接口
            : OnItemTouchCallbackListener?

    /**
     * 滑动或者拖拽的方向，上下左右
     * @param recyclerView 目标RecyclerView
     * @param viewHolder 目标ViewHolder
     * @return 方向
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) { // GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            val dragFlag =
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlag = 0
            return makeMovementFlags(dragFlag, swipeFlag)
        } else if (layoutManager is LinearLayoutManager) { // linearLayoutManager
            val orientation = layoutManager.orientation
            var dragFlag = 0
            var swipeFlag = 0
            if (orientation == LinearLayoutManager.HORIZONTAL) { //横向布局
                swipeFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else if (orientation == LinearLayoutManager.VERTICAL) { //纵向布局
                dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            return makeMovementFlags(dragFlag, swipeFlag)
        }
        return 0
    }

    /**
     * 拖拽item移动时产生回调
     * @param recyclerView 目标RecyclerView
     * @param viewHolder 拖拽的item对应的viewHolder
     * @param target 拖拽目的地的ViewHOlder
     * @return 是否消费拖拽事件
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        callbackListener?.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return false
    }

    /**
     * 滑动删除时回调
     * @param viewHolder 当前操作的Item对应的viewHolder
     * @param direction 方向
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callbackListener?.onSwiped(viewHolder.adapterPosition)
    }

    /**
     * 是否可以长按拖拽
     * @return
     */
    override fun isLongPressDragEnabled(): Boolean {
        return isEnableDrag
    }

    /**
     * 是否可以滑动删除
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return isEnableSwipe
    }

    fun setEnableDrag(enableDrag: Boolean) {
        isEnableDrag = enableDrag
    }

    fun setEnableSwipe(enableSwipe: Boolean) {
        isEnableSwipe = enableSwipe
    }

    /**
     * 手指松开
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        callbackListener?.clearView(recyclerView, viewHolder)
    }

    init {
        this.callbackListener = callbackListener
    }
}

interface OnItemTouchCallbackListener {
    /**
     * 当某个Item被滑动删除时回调
     */
    fun onSwiped(adapterPosition: Int) {}

    /**
     * 当两个Item位置互换的时候被回调(拖拽)
     * @param srcPosition    拖拽的item的position
     * @param targetPosition 目的地的Item的position
     * @return 开发者处理了操作应该返回true，开发者没有处理就返回false
     */
    fun onMove(srcPosition: Int, targetPosition: Int): Boolean {
        return false
    }

    /**
     * 手指松开的时候还原
     * @param recyclerView
     * @param viewHolder
     */
    fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {}

}
