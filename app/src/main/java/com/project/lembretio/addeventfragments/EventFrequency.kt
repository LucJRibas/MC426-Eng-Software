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
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R

class EventFrequency : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var radioMany: RadioButton
    private lateinit var radioOne: RadioButton
    private lateinit var eventCreator: EventCreator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventCreator = context as EventCreator
        val layout = inflater.inflate(R.layout.fragment_event_frequency, container, false)
        nextButton = layout.findViewById(R.id.btn_date_next)
        prevButton = layout.findViewById(R.id.btn_date_prev)
        radioMany = layout.findViewById(R.id.radio_btn_many)
        radioOne = layout.findViewById(R.id.radio_btn_one)

        radioMany.isChecked = eventCreator.event.repeating
        radioOne.isChecked = !radioMany.isChecked

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)
        nextButton.setOnClickListener {
            eventCreator.event.repeating = radioMany.isChecked
            viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!

        }
        prevButton.setOnClickListener {
            eventCreator.event.repeating = radioMany.isChecked
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }
}