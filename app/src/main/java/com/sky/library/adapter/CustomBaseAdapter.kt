package com.sky.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class CustomBaseAdapter<T, VH : RecyclerView.ViewHolder>(private val layoutId: Int, var list: List<T?>?) : RecyclerView.Adapter<VH>() {
    private val clickEventTime = 500
    private var lastClickTime: Long = 0
    private var onItemClickListener: OnItemClickListener<T?>? = null
    private var onItemLongClickListener: OnItemLongClickListener<T?>? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T?>?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T?>?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    protected abstract fun onCreateCustomViewHolder(view: View): VH

    protected abstract fun onBindCustomViewHolder(vh: VH, t: T?)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val vh = onCreateCustomViewHolder(view)
        vh.itemView.setOnClickListener { v: View? ->
            onItemClickListener?.apply {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= clickEventTime) {
                    list?.also {
                        val position = vh.adapterPosition
                        if (position >= 0) {
                            onItemClick(v, it[position], position)
                        }
                    }
                }
                lastClickTime = currentTime
            }
        }
        vh.itemView.setOnLongClickListener { v: View? ->
            var result = false
            onItemLongClickListener?.apply {
                list?.also {
                    val position = vh.adapterPosition
                    if (position >= 0) {
                        result = onItemLongClick(v, it[position], position)
                    }
                }
            }
            result
        }
        return vh
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        list?.also {
            onBindCustomViewHolder(holder, it[position])
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    interface OnItemClickListener<T> {
        fun onItemClick(view: View?, t: T?, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(view: View?, t: T?, position: Int): Boolean
    }
}