package com.dapper.mapboxdemo

/**
 * Created By Tahseen Khan on 18/01/23.
 */

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dapper.mapboxdemo.ui.Dashboard.Companion.longClickListener

class MainAdapter(var list: MutableList<Int>) :
    RecyclerView.Adapter<MainAdapter.ListViewHolder>() /*OnTouchListener*/ {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_layout, parent, false)
        return ListViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvGrid.setImageResource(list[position])
        holder.cvGrid.tag = position
        holder.cvGrid.setOnLongClickListener(longClickListener)
        /*   holder.cvGrid.setOnDragListener(DragListener(listener))*/
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun updateList(list: MutableList<Int>) {
        this.list = list
    }

    /*  val dragInstance: DragListener?
          get() = if (listener != null) {
              DragListener(listener)
          } else {
              null
          }
  */
    inner class ListViewHolder(itemView: View) : ViewHolder(itemView) {
        var tvGrid: ImageView
        var cvGrid: FrameLayout

        init {
            tvGrid = itemView.findViewById(R.id.image)
            cvGrid = itemView.findViewById(R.id.cvGrid)
        }
    }
}