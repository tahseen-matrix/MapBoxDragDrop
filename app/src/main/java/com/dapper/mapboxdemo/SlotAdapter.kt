package com.dapper.mapboxdemo

/**
 * Created By Tahseen Khan on 18/01/23.
 */

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dapper.mapboxdemo.databinding.SlotViewBinding


class SlotAdapter(
    private val arrayList: MutableList<String>,
) : RecyclerView.Adapter<SlotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SlotViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(
        private val itemBinding: SlotViewBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(data: String) {
            itemBinding.textView.text = data
        }
    }
}