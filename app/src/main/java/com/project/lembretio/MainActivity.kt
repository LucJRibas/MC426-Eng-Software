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
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lembretio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isExpanded = false
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }
    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }
    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }
    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_clock)
    }
    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock)
    }
    private val fromBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)
    }
    private val toBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventViewModel.events.observe(this){
            binding.rvEventActivity.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                binding.rvEventActivity.adapter = EventAdapter(it.sortedBy { it.date  }, eventViewModel)
            }
        }

        binding.btnCreateEvent.setOnClickListener {
            if(isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }
        
        binding.btnCreateMedicine.setOnClickListener {
            val intent = Intent(applicationContext, EventPagerActivity::class.java)
            intent.putExtra("is_med", true)
            startActivity(intent)
        }

        binding.btnCreateAppoint.setOnClickListener {
            val intent = Intent(applicationContext, EventPagerActivity::class.java)
            intent.putExtra("is_med", false)
            startActivity(intent)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 122);
        }

        //onBackChange()
    }

    @SuppressLint("NewApi")
    private fun onBackChange() {
        onBackInvokedDispatcher.registerOnBackInvokedCallback(
            OnBackInvokedDispatcher.PRIORITY_DEFAULT
        ) {
            if (isExpanded) {
                shrinkFab()
            } else {
                finish()
            }
        }
    }

    private fun expandFab(){
        binding.btnCreateEvent.startAnimation(rotateClockWiseFabAnim)
        binding.btnCreateEvent.setImageDrawable(resources.getDrawable(R.drawable.baseline_close_24))
        binding.btnCreateAppoint.startAnimation(fromBottomFabAnim)
        binding.btnCreateMedicine.startAnimation(fromBottomFabAnim)
        binding.appointText.startAnimation(fromBottomFabAnim)
        binding.medicineText.startAnimation(fromBottomFabAnim)
        isExpanded = !isExpanded
    }

    private fun shrinkFab(){
        binding.btnCreateEvent.startAnimation(rotateAntiClockWiseFabAnim)
        binding.btnCreateEvent.setImageDrawable(resources.getDrawable(R.drawable.plus_icon))
        binding.btnCreateAppoint.startAnimation(toBottomFabAnim)
        binding.btnCreateMedicine.startAnimation(toBottomFabAnim)
        binding.appointText.startAnimation(toBottomFabAnim)
        binding.medicineText.startAnimation(toBottomFabAnim)
        isExpanded = !isExpanded
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {

            if (isExpanded) {
                val outRect = Rect()
                binding.fabConstraint.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    shrinkFab()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        AlarmReceiver.ringtone?.stop()
    }
}