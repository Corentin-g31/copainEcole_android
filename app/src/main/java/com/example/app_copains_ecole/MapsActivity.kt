package com.example.app_copains_ecole

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.app_copains_ecole.model.UserBean
import com.example.app_copains_ecole.utils.WsUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.concurrent.thread

private const val REFRESH_DATA: Long = 5000
private const val REFRESH_UPDT_LOC: Long = 3000
private const val GROUP1_COLOR = BitmapDescriptorFactory.HUE_BLUE

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    //data
    private val users = ArrayList<UserBean>()
    private lateinit var userConnected: UserBean

    //IHM
    private lateinit var mMap: GoogleMap

    private var firstAnimateCamera = false    //1ere animation déjà faite ?

    // Déclaration d'un pointeur vers un Button / txt ...
    lateinit var btnRefresh: Button
    lateinit var btnSearch: Button
    lateinit var txtSearch: EditText
    lateinit var switch1: Switch
    lateinit var switch2: Switch
    lateinit var switch3: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        userConnected = intent.getSerializableExtra("user") as UserBean

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // bind pointeur et id
        btnRefresh = findViewById(R.id.btnRefresh)
        btnSearch = findViewById(R.id.btnSearch)
        txtSearch = findViewById(R.id.txtSearch)
        switch1 = findViewById(R.id.switch1)
        switch2 = findViewById(R.id.switch2)
        switch3 = findViewById(R.id.switch3)

        // Event listener on btn
        btnRefresh.setOnClickListener(this)
        btnSearch.setOnClickListener(this)
        switch1.setOnClickListener(this)
        switch2.setOnClickListener(this)
        switch3.setOnClickListener(this)


        //Demande permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Pas la permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }


        thread {
            var position: Location? = null
            while (true) {
                position = getLocation()
                if (position != null) {
                    //TODO envoyer au serveur
                    Log.i("tag_i", "UC = $userConnected")
                    userConnected.longitude = position.longitude
                    userConnected.latitude = position.latitude
                    try {
                        WsUtils.setUserCoord(userConnected)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.w("tag_w", "${e.message}")
                        setErrorOnUiThread(e.message)
                    }
                }
                Thread.sleep(REFRESH_UPDT_LOC)
            }
        }
    }

    //callback permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        refreshData()
    }

    //callback de la map
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        thread {
            users.clear()
            users.addAll(WsUtils.getUsers())
            WsUtils.getUsers().forEach { Log.i("tag_i", "${it.pseudo}") }
            refreshData()
        }
    }

    fun refreshData() {
        if (mMap != null) {
            runOnUiThread {

                var locationCamera: LatLng? = null

                //Si permission je sauvegarde coordonnées + j'affiche
                var maPosition: Location? = getLocation()

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                }

                mMap.clear()  //Efface les marker
                // Prepare un groupe de marker
                val latLngBounds = LatLngBounds.Builder()
                users.forEach{
                    //TODO afficher tous les marqueurs
                    val markerOptions = MarkerOptions()
                    markerOptions.position(it.latitude?.let { it1 -> it.longitude?.let { it2 -> LatLng(it1, it2) } })
                    markerOptions.title(it.pseudo)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    mMap.addMarker(markerOptions)
                }

                // Zoom sur le groupe de markers
                if (switch1.isChecked && users.size > 1) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 500))
                } else if (maPosition != null) {
                    // centre et zoom camera
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(maPosition.toLatLng(), 10F))
                }
            }
        }
    }

    fun Location.toLatLng() = LatLng(this.latitude,this.longitude)

    fun getLocation(): Location? {
        //Si permission je sauvegarde coordonnées + j'affiche
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Récupération de la localisation
            val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.getBestProvider(Criteria(), false)?.let { lm.getLastKnownLocation(it) }
        }
        return null
    }

    override fun onClick(v: View?) {
        when (v) {
            btnRefresh -> {
                Log.i("tag_i", "onClick: btnRefresh")
            }
            btnSearch -> {
                Log.i("tag_i", "onClick: btnSearch")
            }
            switch1 -> {
                Log.i("tag_i", "onClick: switch1, state : ${switch1.isChecked}")
                // Todo to ameliorate
            }
            switch2 -> {
                Log.i("tag_i", "onClick: switch2, state : ${switch2.isChecked}")
                // Todo method getUsers for group 2
            }
            switch3 -> {
                Log.i("tag_i", "onClick: switch3, state : ${switch3.isChecked}")
                // Todo method getUsers for group 3
            }
        }
    }

    fun setErrorOnUiThread(text: String?) = runOnUiThread {
        if (!text.isNullOrBlank()) {
            Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
        }
    }
}