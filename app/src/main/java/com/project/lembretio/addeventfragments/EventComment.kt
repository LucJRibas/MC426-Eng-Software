package com.project.lembretio.addeventfragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R


class EventComment : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var editText: EditText
    private lateinit var titleText: TextView
    private lateinit var eventCreator: EventCreator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventCreator = context as EventCreator
        val layout = inflater.inflate(R.layout.fragment_event_comment, container, false)
        nextButton = layout.findViewById(R.id.btn_title_next)
        prevButton = layout.findViewById(R.id.btn_title_prev)
        editText = layout.findViewById(R.id.edit_text_comment)
        titleText = layout.findViewById(R.id.event_comment_text)


        editText.setText(eventCreator.event.comment)

        editText.doAfterTextChanged {
            eventCreator.event.comment = it.toString()
        }

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)
        nextButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
        }
        prevButton.setOnClickListener {
            activity?.finish()
        }
        return layout
    }
}