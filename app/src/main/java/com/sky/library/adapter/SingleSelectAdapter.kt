package com.sky.library.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class SingleSelectAdapter<T, VH : RecyclerView.ViewHolder>(layoutId: Int, list: List<T?>) : CustomBaseAdapter<T, VH>(layoutId, list) {
    var currentPos = -1
        private set
    private var previousPos = -1
    private var selectedListener: OnItemSelectedListener<T?>? = null
    private var unSelectedColor = Color.TRANSPARENT
    private var selectedColor = Color.TRANSPARENT
    private var unSelectedDrawable: Drawable? = null
    private var selectedDrawable: Drawable? = null

    fun selectItem(position: Int) {
        if (position != currentPos) {
            previousPos = currentPos
            currentPos = position
            notifyItemChanged(previousPos)
            notifyItemChanged(currentPos)
        }
    }

    fun setOnItemSelectedListener(listener: OnItemSelectedListener<T?>?) {
        selectedListener = listener
    }

    fun setStateColors(colorSelected: Int, colorUnselected: Int) {
        selectedColor = colorSelected
        unSelectedColor = colorUnselected
    }

    fun setStateDrawables(drawableSelected: Drawable?, drawableUnselected: Drawable?) {
        selectedDrawable = drawableSelected
        unSelectedDrawable = drawableUnselected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val result = super.onCreateViewHolder(parent, viewType)
        //Set the on item click again
        setOnItemClickListener(object : OnItemClickListener<T?> {
            override fun onItemClick(view: View?, t: T?, position: Int) {
                selectedListener?.apply {
                    onItemSelected(view, t, position)
                }
            }
        })
        return result
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            //current
            if (adapterPosition == currentPos) {
                selectedDrawable?.let {
                    itemView.background = it
                } ?: run {
                    itemView.setBackgroundColor(selectedColor)
                }
            } else {
                //default
                unSelectedDrawable?.let {
                    itemView.background = it
                } ?: run {
                    itemView.setBackgroundColor(unSelectedColor)
                }
            }
        }
    }

    interface OnItemSelectedListener<T> {
        fun onItemSelected(view: View?, t: T?, position: Int)
    }
}
