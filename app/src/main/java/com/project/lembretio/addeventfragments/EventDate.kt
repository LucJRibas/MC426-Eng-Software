package com.project.lembretio.addeventfragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class EventDate : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var dateButton: Button
    private lateinit var dateText: TextView
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_event_date, container, false)
        nextButton = layout.findViewById(R.id.btn_title_next)
        prevButton = layout.findViewById(R.id.btn_title_prev)
        dateButton = layout.findViewById(R.id.btn_date_pick)
        titleText = layout.findViewById(R.id.date_title_text)
        dateText = layout.findViewById(R.id.tv_date)

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)

        var date: LocalDate? = null

        if ((context as EventCreator).date != "") {
            date = LocalDate.parse((context as EventCreator).date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            dateText.text = (context as EventCreator).date

        }

        dateButton.setOnClickListener {
            context?.let {
                val setDateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    date = LocalDate.of(year, month + 1, dayOfMonth)
                    dateText.text = date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }

                val today = LocalDate.now()
                val datePickerDialog = DatePickerDialog(it, setDateListener, today.year, today.monthValue, today.dayOfYear)
                datePickerDialog.setCancelable(false)
                datePickerDialog.show()
            }
        }

        nextButton.setOnClickListener {
            if (date != null) {
                if (date!!.isBefore(LocalDate.now())) {
                    Toast.makeText(context, "Por favor selecione uma data futura", Toast.LENGTH_SHORT).show()
                } else {
                    (context as EventCreator).date = date!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
                }
            } else  {
                Toast.makeText(context, "Por favor selecione uma data", Toast.LENGTH_SHORT).show()
            }
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }

    override fun onResume() {
        super.onResume()
        if ((context as EventCreator).repeating) {
            titleText.text = "A partir de qual data você começará a tomar o medicamento?"

        } else {
            titleText.text = "Qual será o dia do medicamento?"

        }
    }
}