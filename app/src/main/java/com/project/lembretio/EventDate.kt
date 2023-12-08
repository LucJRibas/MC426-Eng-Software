package com.project.lembretio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2

class EventDate : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_event_date, container, false)
        nextButton = layout.findViewById<Button>(R.id.btn_date_next)
        prevButton = layout.findViewById<Button>(R.id.btn_date_prev)
        var viewPager: ViewPager2? = activity?.findViewById<ViewPager2>(R.id.view_pager)
        nextButton.setOnClickListener {
            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            EventDate()
    }
}