package com.dapper.mapboxdemo.ui

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dapper.mapboxdemo.DragAdapter
import com.dapper.mapboxdemo.MainAdapter
import com.dapper.mapboxdemo.R
import com.dapper.mapboxdemo.SlotAdapter
import com.dapper.mapboxdemo.databinding.ActivityDashboardBinding
import com.dapper.mapboxdemo.utils.Listener
import com.mapbox.maps.MapView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Dashboard : AppCompatActivity(), Listener {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    val arrayList: MutableList<String> = ArrayList<String>()
    private val intArrayList: MutableList<Int?> = ArrayList<Int?>()
    var adapter: DragAdapter? = null
    private var bottomListAdapter: MainAdapter? = null

    //mapbox
    private var mapView: MapView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mapView = binding.mapView

        val calendar = Calendar.getInstance()
        val today = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("MMMM d, yyyy")

        val todayAsString: String = dateFormat.format(today)
        val dayFormat: DateFormat = SimpleDateFormat("EEEE")
        binding.dateLayout.dateTv.text = todayAsString
        binding.dateLayout.dayTv.text = dayFormat.format(today)

        clickHandle(calendar, dateFormat, dayFormat)
        setTimeSlots()
        setBottomRecyclerView()
        parralelScroll()
    }

    private fun clickHandle(calendar: Calendar, dateFormat: DateFormat, dayFormat: DateFormat) {
        binding.dateLayout.preBtn.setOnClickListener {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = calendar.time
            val yesterdayAsString: String = dateFormat.format(yesterday)
            binding.dateLayout.dateTv.text = yesterdayAsString
            binding.dateLayout.dayTv.text = dayFormat.format(yesterday)
        }
        binding.dateLayout.nextBtn.setOnClickListener {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = calendar.time
            val tomorrowAsString: String = dateFormat.format(tomorrow)
            binding.dateLayout.dateTv.text = tomorrowAsString
            binding.dateLayout.dayTv.text = dayFormat.format(tomorrow)
        }

    }

    private fun parralelScroll() {
        val scrollListeners = arrayOfNulls<RecyclerView.OnScrollListener>(2)
        scrollListeners[0] = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollListeners[1]?.let { binding.Rv.removeOnScrollListener(it) }
                binding.Rv.scrollBy(dx, dy)
                scrollListeners[1]?.let { binding.Rv.addOnScrollListener(it) }
            }
        }
        scrollListeners[1] = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollListeners[0]?.let { binding.slotRv.removeOnScrollListener(it) }
                binding.slotRv.scrollBy(dx, dy)
                scrollListeners[0]?.let { binding.slotRv.addOnScrollListener(it) }
            }
        }
        scrollListeners[0]?.let { binding.slotRv.addOnScrollListener(it) }
        scrollListeners[1]?.let { binding.Rv.addOnScrollListener(it) }
    }

    var bottomList: MutableList<Int> = ArrayList()
    private fun setBottomRecyclerView() {
        binding.scheduleLayout.scheduleRv.setHasFixedSize(true)
        binding.scheduleLayout.scheduleRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        bottomList.add(R.drawable.one)
        bottomList.add(R.drawable.two)
        bottomList.add(R.drawable.three)

        bottomListAdapter = MainAdapter(bottomList)
        binding.scheduleLayout.scheduleRv.adapter = bottomListAdapter
    }

    companion object {

        val longClickListener = View.OnLongClickListener { v ->
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(v)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v?.startDragAndDrop(data, shadowBuilder, v, 0)
            } else {
                v?.startDrag(data, shadowBuilder, v, 0)
            }
            true
        }
    }

    var draggedItemIndex: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTimeSlots() {
        val firstDate = "18/01/2023"
        val firstTime = "08:00 AM"
        val secondDate = "18/01/2023"
        val secondTime = "08:15 PM"

        val format = "dd/MM/yyyy hh:mm a"

        val sdf = SimpleDateFormat(format)

        val dateObj1 = sdf.parse("$firstDate $firstTime")
        val dateObj2 = sdf.parse("$secondDate $secondTime")
        println("Date Start: $dateObj1")
        println("Date End: $dateObj2")

        var dif = dateObj1.time
        while (dif < dateObj2.time) {
            val slot = Date(dif)
            val sFormat = "hh:mm a"
            val dateFormat = SimpleDateFormat(sFormat, Locale.US)
            println("Hour Slot --->${dateFormat.format(slot)}")
            arrayList.add(dateFormat.format(slot))
            dif += (15 * 60000)
        }

        binding.slotRv.adapter = SlotAdapter(arrayList)

        for (i in arrayList.indices) {
            intArrayList.add(null)
        }
        adapter = DragAdapter(intArrayList, this)
        binding.Rv.setHasFixedSize(true)
        binding.Rv.adapter = adapter

        //itemtouch listener
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                return makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    0
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {

                draggedItemIndex = viewHolder.adapterPosition
                val targetIndex = target.adapterPosition
                if (targetIndex != 0 && draggedItemIndex != 0) {
                    Collections.swap(intArrayList, draggedItemIndex!!, targetIndex)
                    adapter!!.notifyItemMoved(draggedItemIndex!!, targetIndex)
                }
                Log.e("Dashboard",
                    "on move call drag index $draggedItemIndex target index $targetIndex")
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

        })

        itemTouchHelper.attachToRecyclerView(binding.Rv)

    }

    override fun setUpdateItem(list: Int, position: Int) {
        intArrayList[position] = list
        adapter!!.notifyItemChanged(position)
        Log.e("Dashboard", "new list $intArrayList")
    }


}