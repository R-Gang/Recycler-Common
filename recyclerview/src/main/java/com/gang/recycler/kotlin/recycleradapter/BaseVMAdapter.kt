package com.gang.recycler.kotlin.recycleradapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gang.recycler.kotlin.interfaces.ViewOnItemClick
import com.gang.recycler.kotlin.interfaces.ViewOnItemLongClick
import java.lang.reflect.ParameterizedType

/**
 * @CreateDate:     2022/7/15 10:54
 * @Author:         haoruigang
 * @ClassName:      BaseVMAdapter
 * @Description:    Adapter封装
 */
abstract class BaseVMAdapter<VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseViewHolder<VB>> {

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
    ) : this(
        `object`
    ) {
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
    ): BaseViewHolder<VB> {
        val binding = initViewBinding(viewGroup) as VB
        return BaseViewHolder(binding, onItemClick, longClick)
    }

    //将数据与界面进行绑定的操作
    override fun onBindViewHolder(
        viewHolder: BaseViewHolder<VB>,
        position: Int,
    ) {
        convert(viewHolder, position, mContext)
    }

    abstract fun convert(
        holder: BaseViewHolder<VB>,
        position: Int,
        context: Context,
    )

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

class BaseViewHolder<VB : ViewDataBinding>(
    binding: VB,
    var onItemClick: ViewOnItemClick?,
    var longClick: ViewOnItemLongClick?,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    val mBinding = binding

    override fun onClick(v: View) {
        onItemClick?.setOnItemClickListener(v, position)
    }

    override fun onLongClick(v: View): Boolean {
        longClick?.setOnItemLongClickListener(v, position)
        return true
    }

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }
}

/**
 * 初始化 [BaseVMAdapter] 中的 [ViewDataBinding]
 * 适用于范型参数为 [ViewDataBinding] 的 [Adapter]
 */
fun <VB : ViewDataBinding> BaseVMAdapter<VB>.initViewBinding(
    container: ViewGroup,
): VB? {
    var viewBinding: VB? = null
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
        val clazz = type.actualTypeArguments[0] as? Class<VB>
        val method = clazz?.getMethod("inflate", LayoutInflater::class.java)
        viewBinding = (method?.invoke(null, LayoutInflater.from(container.context)) as? VB)
    }
    return viewBinding
}