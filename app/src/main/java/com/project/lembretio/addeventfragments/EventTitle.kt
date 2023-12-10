package com.project.lembretio.addeventfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R

class EventTitle : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var editText: EditText
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_event_title, container, false)
        nextButton = layout.findViewById(R.id.btn_title_next)
        prevButton = layout.findViewById(R.id.btn_title_prev)
        editText = layout.findViewById(R.id.edit_text_title)
        titleText = layout.findViewById(R.id.event_title_text)


        editText.setText((context as EventCreator).name)

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)
        nextButton.setOnClickListener {
            if (editText.text.toString() == "") {
                Toast.makeText(context, "Por favor coloque um nome de remédio", Toast.LENGTH_SHORT).show()
            } else {
                (context as EventCreator).name = editText.text.toString()
                viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
            }
        }
        prevButton.setOnClickListener {
            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        return layout
    }

    override fun onResume() {
        super.onResume()
        if ((context as EventCreator).isMedication) {
            titleText.text = "Qual é o remédio que você precisa tomar?"
            editText.hint = "Digite o nome dele aqui!"

        } else {
            titleText.text = "Qual será a consulta?"
            editText.hint = "Digite título da consulta aqui!"

        }
    }
}