package com.example.memecreator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memecreator.R
import com.example.memecreator.db.models.meme.MemeTemplate

abstract class MemeAdapterTemplate : RecyclerView.Adapter<MemeAdapterTemplate.MemeViewHolder>() {
    inner class MemeViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    protected var onMyMemeClickListener: ((MemeTemplate) -> Unit)? = null

    fun setOnMemeClickListener(func: ((MemeTemplate) -> Unit)) {
        onMyMemeClickListener = func
    }

    private val differCallback = object : DiffUtil.ItemCallback<MemeTemplate>() {
        override fun areItemsTheSame(oldItem: MemeTemplate, newItem: MemeTemplate): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MemeTemplate, newItem: MemeTemplate): Boolean {
            return oldItem.uril == newItem.uril
        }
    }

    val diffUtil = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        return MemeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_meme, parent, false)
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}