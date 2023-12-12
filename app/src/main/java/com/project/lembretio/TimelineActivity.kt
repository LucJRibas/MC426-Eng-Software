package com.project.lembretio


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lembretio.databinding.ActivityMainBinding

class TimelineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        // Receba o evento da Intent
        val receivedEvent: Event? = intent.getParcelableExtra("event")

        val button = findViewById<LinearLayout>(R.id.llEventInfo)
        val titleText = findViewById<TextView>(R.id.remedio)
        val dateinit = findViewById<TextView>(R.id.inicio)
        val timing = findViewById<TextView>(R.id.progresso)
        val alarme = findViewById<TextView>(R.id.alarme)





    }
}