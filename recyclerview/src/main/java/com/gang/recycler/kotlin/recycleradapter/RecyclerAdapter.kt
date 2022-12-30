package com.gang.recycler.kotlin.recycleradapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.interfaces.ViewOnItemLongClick

abstract class RecyclerAdapter : RecyclerView.Adapter<RecyclerViewHolder> {

    var datas = mutableListOf<Any>()
    var onItemClick: ViewOnItemClick? = null
    var longClick: ViewOnItemLongClick? = null
    private var mObject: Any? = null
    lateinit var mContext: Context

    constructor(`object`: Any) {
        instanceofObj(`object`)
    }

    constructor(
        datas: MutableList<*>? = mutableListOf<Any>(),
        `object`: Any
    ) : this(`object`) {
        this.datas.clear()
        this.datas.addAll(datas as ArrayList<*>)
    }

    constructor(
        datas: MutableList<*>? = mutableListOf<Any>(),
        `object`: Any,
        onItemClick1: ViewOnItemClick,
    ) : this(datas, `object`) {
        onItemClick = onItemClick1
    }

    constructor(
        datas: MutableList<*>? = mutableListOf<Any>(),
        `object`: Any,
        onItemClick1: ViewOnItemClick,
        onItemLongClick: ViewOnItemLongClick,
    ) : this(datas, `object`, onItemClick1) {
        longClick = onItemLongClick
    }

    private fun instanceofObj(`object`: Any) {
        mObject = `object`
        when (`object`) {
            is Fragment -> {
                mContext = `object`.requireContext()
            }
            is Activity -> {
                mContext = `object`
            }
            is View -> {
                mContext = `object`.context
            }
        }
    }

    //创建新View，被LayoutManager所调用
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): RecyclerViewHolder {
        val res: Int = layoutResId
        val view = LayoutInflater.from(viewGroup.context).inflate(res, viewGroup, false)
        return RecyclerViewHolder(view, onItemClick, longClick)
    }

    //将数据与界面进行绑定的操作
    override fun onBindViewHolder(
        viewHolder: RecyclerViewHolder,
        position: Int,
    ) {
        convert(viewHolder, position, mContext)
    }

    abstract fun convert(
        holder: RecyclerViewHolder,
        position: Int,
        context: Context,
    )

    abstract val layoutResId: Int

    //获取数据的数量
    override fun getItemCount(): Int {
        return if (datas.isEmpty()) 0 else datas.size
    }

    open fun update(pdata: List<*>?, isClear: Boolean = true) {
        if (isClear) {
            this.datas.clear()
        }
        this.datas.addAll(pdata as ArrayList<*>)
        notifyDataSetChanged()
    }

}

/**
 * Created by 1bu2bu-4 on 2016/9/1.
 */
class RecyclerViewHolder(
    var view: View,
    onItemClick: ViewOnItemClick?,
    longClick: ViewOnItemLongClick?,
) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
    var onItemClick: ViewOnItemClick? = onItemClick
    var longClick: ViewOnItemLongClick? = longClick
    override fun onClick(v: View) {
        onItemClick?.setOnItemClickListener(v, position)
    }

    override fun onLongClick(v: View): Boolean {
        longClick?.setOnItemLongClickListener(v, position)
        return true
    }

    fun getView(id: Int): View {
        return view.findViewById(id)
    }

    fun <T : View> getView2(id: Int): T {
        return view.findViewById(id) as T
    }

    init {
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
    }
}