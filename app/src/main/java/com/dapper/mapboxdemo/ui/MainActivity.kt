package com.dapper.mapboxdemo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.dapper.mapboxdemo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.gestures
import org.json.JSONObject
import java.util.*

@SuppressLint("Lifecycle")
class MainActivity : AppCompatActivity() {
    private var mapView: MapView? = null
    private var annotationApi: AnnotationPlugin? = null
    private lateinit var annotationConfig: AnnotationConfig
    private var layerIOD = "map_annotation" //hard core value
    private var pointAnnotationManager: PointAnnotationManager? = null

    //marker list for displaying multiple marker
    private var markerList: ArrayList<PointAnnotationOptions> = ArrayList()

    //Just for testing
    private var latitudeList: ArrayList<Double> = ArrayList()
    private var longitudeList: ArrayList<Double> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)

        createLatLongForMarker()

        mapView?.getMapboxMap()!!.loadStyleUri(Style.MAPBOX_STREETS
        ) {
            zoomCamera()
            //now add marker
            annotationApi = mapView?.annotations
            annotationConfig = AnnotationConfig(
                layerId = layerIOD
            )
            pointAnnotationManager = annotationApi?.createPointAnnotationManager(annotationConfig)

            createMarkerList()

            try {
                mapView!!.gestures.pitchEnabled = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun zoomCamera() {
        mapView!!.getMapboxMap().setCamera(
            CameraOptions.Builder().center(Point.fromLngLat(76.6853675, 30.6913459))
                .zoom(13.0).build()
        )
    }

    //Create dummy list of lat long
    private fun createLatLongForMarker() {
        latitudeList.add(30.7000166)
        longitudeList.add(76.6905748)

        latitudeList.add(30.7052158)
        longitudeList.add(76.6839974)

        latitudeList.add(30.684585)
        longitudeList.add(76.6837238)

        latitudeList.add(30.6955456)
        longitudeList.add(76.68476)

    }

    private fun clearAnnotation() {
        markerList = ArrayList()
        pointAnnotationManager?.deleteAll()
    }

    private fun createMarkerList() {

        clearAnnotation()

        // It will work when we create marker
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener { annotation: PointAnnotation ->
            onMarkerItemClick(annotation)
            true
        })


        markerList = ArrayList()
        val bitmap =
            convertDrawableToBitmap(AppCompatResources.getDrawable(this, R.drawable.ic_location))

        for (i in latitudeList.indices) {

            val mObe = JSONObject()
            mObe.put("some-key", i)

            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitudeList[i], latitudeList[i]))
                .withData(Gson().fromJson(mObe.toString(), JsonElement::class.java))
                .withIconImage(bitmap!!)
                .withIconSize(2.0)

            markerList.add(pointAnnotationOptions)
        }

        pointAnnotationManager?.create(markerList)

    }

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    private fun onMarkerItemClick(marker: PointAnnotation) {

        val jsonElement: JsonElement? = marker.getData()

        val position: Int = (jsonElement as JsonObject).get("some-key").asInt
        try {
            val geoUri =
                "http://maps.google.com/maps?q=loc:" + latitudeList[position].toString() + "," + longitudeList[position].toString() + " (" + "Location " + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            startActivity(intent)
        } catch (_: java.lang.Exception) {

        }

    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}