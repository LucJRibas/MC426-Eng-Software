package com.project.lembretio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2

class EventTitle : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_event_title, container, false)
        nextButton = layout.findViewById<Button>(R.id.btn_title_next)
        prevButton = layout.findViewById<Button>(R.id.btn_title_prev)
        var viewPager: ViewPager2? = activity?.findViewById<ViewPager2>(R.id.view_pager)
        nextButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
        }
        prevButton.setOnClickListener {
            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        return layout
    }

    companion object {
        fun newInstance() =
            EventTitle()
    }
}