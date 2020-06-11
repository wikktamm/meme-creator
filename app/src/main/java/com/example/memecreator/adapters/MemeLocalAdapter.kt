package com.example.memecreator.adapters

import coil.api.load
import com.example.memecreator.db.models.meme.Meme
import com.example.memecreator.db.models.meme.MemeLocal
import kotlinx.android.synthetic.main.row_meme.view.*

class MemeLocalAdapter : MemeAdapterTemplate() {
    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val currentMeme = diffUtil.currentList[position] as MemeLocal
        holder.item.apply {
            ivMeme.load(currentMeme.uri)
            setOnClickListener {
                onMyMemeClickListener?.let {
                    it(currentMeme)
                }
            }
        }
    }
}