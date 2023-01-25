package com.dapper.mapboxdemo

/**
 * Created By Tahseen Khan on 18/01/23.
 */

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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
    var arrayList: MutableList<Int?>, var listener: Listener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnTouchListener {
    val HEADERVIEW = 0
    val LISTVIEW = 1


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

    fun updateItem(list: Int, position: Int) {
        arrayList.add(position, list)
    }

    fun getDragInstance(): DragListener? {
        return if (listener != null) {
            DragListener(listener)
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!")
            null
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
                if (arrayList[position] != null) {
                    listHolder.tvGrid.visibility = View.VISIBLE
                    arrayList[position]?.let {
                        listHolder.tvGrid.setImageResource(it)
                    }
                } else {
                    listHolder.tvGrid.visibility = View.GONE
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
        private val itemBinding: ImageLayoutBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        var tvGrid: ImageView = itemBinding.image
        var cvGrid: FrameLayout = itemBinding.cvGrid


    }

    class HeaderViewHoler(
        private val itemBinding: StartEndLayoutBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        val titleTv = itemBinding.titleTv
        val startEndTv = itemBinding.startEndTv
        val iconIVTv = itemBinding.iconIVTv

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(view)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0)
            } else {
                view.startDrag(data, shadowBuilder, view, 0)
            }
            return true
        }
        return false
    }
}