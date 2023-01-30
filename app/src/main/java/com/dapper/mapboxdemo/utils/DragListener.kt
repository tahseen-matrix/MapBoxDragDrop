package com.dapper.mapboxdemo.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dapper.mapboxdemo.DragAdapter
import com.dapper.mapboxdemo.MainAdapter
import com.dapper.mapboxdemo.R


/**
 * Created By Tahseen Khan on 24/01/23.
 */
class DragListener internal constructor(listener: Listener) : View.OnDragListener {
    private var isDropped = false
    private val listener: Listener

    init {
        this.listener = listener
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                isDropped = true
                val view = event.localState as View
                if (view.id == R.id.cvGrid && v.id == R.id.cvGrid) {
                    // resetAdapter(view)
                    val source = view.parent as RecyclerView
                    val bottomListAdapter = source.adapter as MainAdapter
                    val positionSource = view.tag as Int
                    val sourceId: Int = source.id
                    val list = bottomListAdapter!!.list[positionSource]
                    val listSource: MutableList<Int> = bottomListAdapter.list
                    listSource.removeAt(positionSource)
                    bottomListAdapter.notifyItemRemoved(positionSource)
                    bottomListAdapter.notifyItemRangeChanged(positionSource, listSource.size)

                    val target = v.parent as RecyclerView
                    val topAdapter = target.adapter as DragAdapter

                    listener.setUpdateItem(list, target.getChildAdapterPosition(v))
                    Log.e("Dashboard", "${target.getChildAdapterPosition(v)}")

                    v.background.clearColorFilter()
                    v.invalidate()

                }

            }
            DragEvent.ACTION_DRAG_ENDED->{
                v.background.clearColorFilter()
                v.invalidate()
                if (event.result)
                    Log.e("Dashboard", "The drop was handled.")
                else
                    Log.e("Dashboard", "The drop didn't work.")
            }
            DragEvent.ACTION_DRAG_LOCATION->{

            } // this will show the location of the item that is being dragged
            DragEvent.ACTION_DRAG_STARTED->{
                v.background.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
            }
        }
        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true
    }
}