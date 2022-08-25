package com.recycler.kotlin.demo

import android.content.Context
import com.gang.recycler.kotlin.recycleradapter.BaseVMAdapter
import com.gang.recycler.kotlin.recycleradapter.BaseViewHolder
import com.recycler.kotlin.databinding.ItemHomeMenuBinding

/**
 *
 * @ProjectName:    Recycler-Common
 * @Package:        com.recycler.kotlin.demo
 * @ClassName:      HomeMenuAdapter
 * @Description:    适配器基础使用方式
 * @Author:         haoruigang
 * @CreateDate:     2020/8/5 16:00
 */
class HomeMenuAdapter(
    datas: MutableList<*>, `object`: Any, override val layoutResId: Int,
) :
    BaseVMAdapter<ItemHomeMenuBinding>(datas, `object`) {

    var homeIcon: HomeIcon? = null

    override fun convert(
        holder: BaseViewHolder<ItemHomeMenuBinding>,
        position: Int,
        context: Context,
    ) {
        holder.mBinding.apply {
            homeIcon = datas[position] as HomeIcon

            homeIcon?.icon?.toInt()?.let { ivIcon.setImageResource(it) }
            tvName.text = homeIcon?.name
        }
    }
}