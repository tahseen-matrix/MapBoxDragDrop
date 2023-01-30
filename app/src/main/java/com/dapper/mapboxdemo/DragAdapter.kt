package com.dapper.mapboxdemo

/**
 * Created By Tahseen Khan on 18/01/23.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dapper.mapboxdemo.databinding.ImageLayoutBinding
import com.dapper.mapboxdemo.databinding.StartEndLayoutBinding
import com.dapper.mapboxdemo.utils.DragListener
import com.dapper.mapboxdemo.utils.Listener


class DragAdapter(
    var arrayList: MutableList<Int?>, var listener: Listener,onClickListener: RecyclerClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val HEADERVIEW = 0
    val LISTVIEW = 1
    private var onClickListener: RecyclerClickListener? = null
    interface RecyclerClickListener{
        fun onClickListener(image:Int, pos:Int)
    }
    init {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (getItemViewType(viewType)) {
            HEADERVIEW -> {
                return HeaderViewHoler(
                    StartEndLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                val view = ImageLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                view.root.setOnDragListener(DragListener(listener))
                return ListViewHolder(
                    view
                )
            }
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADERVIEW -> {
                val headerViewHoler = holder as HeaderViewHoler
                if (position == arrayList.size - 1) {
                    headerViewHoler.startEndTv.text = "End"
                    headerViewHoler.apply {
                        startEndTv.setTextColor(ContextCompat.getColor(startEndTv.context,
                            R.color.red))
                        titleTv.setTextColor(ContextCompat.getColor(startEndTv.context,
                            R.color.red))
                        iconIVTv.setColorFilter(ContextCompat.getColor(startEndTv.context,
                            R.color.red))
                    }
                } else {
                    headerViewHoler.startEndTv.text = "Start"
                    headerViewHoler.apply {
                        startEndTv.setTextColor(ContextCompat.getColor(startEndTv.context,
                            R.color.green))
                        titleTv.setTextColor(ContextCompat.getColor(startEndTv.context,
                            R.color.green))
                        iconIVTv.setColorFilter(ContextCompat.getColor(startEndTv.context,
                            R.color.green))
                    }
                }
                headerViewHoler.titleTv.text = "Ambala Cantonment Junction railway station...."
            }

            LISTVIEW -> {
                val listHolder = holder as ListViewHolder
                listHolder.itemView.background.clearColorFilter()
                listHolder.itemView.invalidate()
                if (arrayList[position] != null) {
                    listHolder.tvGrid.visibility = View.VISIBLE
                    listHolder.itemView.background.clearColorFilter()
                    listHolder.itemView.invalidate()
                    arrayList[position]?.let {
                        listHolder.tvGrid.setImageResource(it)
                    }
                    listHolder?.tvGrid?.setOnClickListener { arrayList[position]?.let { it1 ->
                        onClickListener?.onClickListener(it1, position)
                    } }
                } else {
                    listHolder.tvGrid.visibility = View.GONE
                    listHolder.itemView.background.clearColorFilter()
                    listHolder.itemView.invalidate()
                }
            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position == arrayList.size - 1) {
            return HEADERVIEW
        }
        return LISTVIEW
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ListViewHolder(
        itemBinding: ImageLayoutBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        var tvGrid: ImageView = itemBinding.image
    }

    class HeaderViewHoler(
        itemBinding: StartEndLayoutBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        val titleTv = itemBinding.titleTv
        val startEndTv = itemBinding.startEndTv
        val iconIVTv = itemBinding.iconIVTv

    }

}