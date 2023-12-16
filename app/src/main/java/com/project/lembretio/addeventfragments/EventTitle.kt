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
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import com.project.lembretio.utils.isEventNameValid


class EventTitle : Fragment() {

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
        val layout = inflater.inflate(R.layout.fragment_event_title, container, false)
        nextButton = layout.findViewById(R.id.btn_title_next)
        prevButton = layout.findViewById(R.id.btn_title_prev)
        editText = layout.findViewById(R.id.edit_text_title)
        titleText = layout.findViewById(R.id.event_title_text)


        editText.setText(eventCreator.event.name)

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)
        nextButton.setOnClickListener {
            eventCreator.event.name = editText.text.toString()
            if (!isEventNameValid(eventCreator.event)) {
                if (eventCreator.event.isMedication) {
                    Toast.makeText(
                        context,
                        "Por favor coloque um nome de remédio",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Por favor coloque o nome da consulta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {


                val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                var view = activity?.currentFocus
                if (view == null) {
                    view = View(activity)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)

                viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
            }
        }
        prevButton.setOnClickListener {
            activity?.finish()
        }
        return layout
    }

    override fun onResume() {
        super.onResume()
        if (eventCreator.event.isMedication) {
            titleText.text = "Qual é o remédio que você precisa tomar?"
            editText.hint = "Digite o nome dele aqui!"

        } else {
            titleText.text = "Qual será a consulta?"
            editText.hint = "Digite título da consulta aqui!"

        }
    }
}