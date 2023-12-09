package com.project.lembretio.addeventfragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.MainActivity
import com.project.lembretio.R

class EventFrequency : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var radioMany: RadioButton
    private lateinit var radioWeek: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_event_frequency, container, false)
        nextButton = layout.findViewById<Button>(R.id.btn_date_next)
        prevButton = layout.findViewById<Button>(R.id.btn_date_prev)
        radioMany = layout.findViewById<RadioButton>(R.id.radio_btn_many)
        radioWeek = layout.findViewById<RadioButton>(R.id.radio_btn_week)
        var viewPager: ViewPager2? = activity?.findViewById<ViewPager2>(R.id.view_pager)
        nextButton.setOnClickListener {
            if(radioMany.isChecked) {
                viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
            } else if(radioWeek.isChecked){
                viewPager?.currentItem = viewPager?.currentItem?.plus(2)!!
            } else {
                val intentBack = Intent(context, MainActivity::class.java)
                startActivity(intentBack)
            }
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            EventFrequency()
    }
}