package com.example.memecreator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.memecreator.R
import com.example.memecreator.db.models.meme.Meme
import kotlinx.android.synthetic.main.row_meme.view.*

class MemeAdapter :RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {
    inner class MemeViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    val differCallback = object : DiffUtil.ItemCallback<Meme>(){
        override fun areItemsTheSame(oldItem: Meme, newItem: Meme): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Meme, newItem: Meme): Boolean {
            return oldItem.url == newItem.url
        }
    }

    val diffUtil = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        return MemeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_meme, parent, false))
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val currentMeme = diffUtil.currentList[position]
        holder.item.ivMeme.load(currentMeme.url)
    }
}