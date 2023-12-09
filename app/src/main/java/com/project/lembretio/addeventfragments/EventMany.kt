package com.project.lembretio.addeventfragments

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import java.util.Calendar


class EventMany : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var spinner: Spinner
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_event_many, container, false)
        nextButton = layout.findViewById<Button>(R.id.btn_many_next)
        prevButton = layout.findViewById<Button>(R.id.btn_many_prev)
        spinner = layout.findViewById<Spinner>(R.id.spinner_many)
        recycler = layout.findViewById<RecyclerView>(R.id.recycler_many)
        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = manyAdapter()
        recycler.adapter = adapter
        populateSpinner()
        var viewPager: ViewPager2? = activity?.findViewById<ViewPager2>(R.id.view_pager)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })
        nextButton.setOnClickListener {
            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }

    private fun populateSpinner(){
        val items: MutableList<Int> = MutableList<Int>(10) {index -> 1 + index}
        val adapter: ArrayAdapter<Int>? =
            context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_spinner_item, items) }
        spinner.adapter = adapter
    }

    companion object {
        fun newInstance() =
            EventMany()
    }

    private inner class manyAdapter : RecyclerView.Adapter<manyAdapter.manyHolder>(){

        private inner class manyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): manyHolder {
            return manyHolder(
                layoutInflater.inflate(R.layout.single_date, parent, false)
            )
        }

        override fun onBindViewHolder(holder: manyHolder, position: Int) {
            holder.itemView.apply {
                val button = findViewById<Button>(R.id.single_date_btn)
                val text = findViewById<TextView>(R.id.single_date_text)
                text.text = position.toString()
                button.setOnClickListener {
                    val cal : Calendar = Calendar.getInstance()
                    var setTimeListener = TimePickerDialog.OnTimeSetListener{
                        timePicker, hour, minute ->
                        val hour = String.format("%02d", hour);
                        val minute = String.format("%02d", minute)
                        text.text = hour+":"+minute
                    }
                    val timePickerDialog = TimePickerDialog(context,setTimeListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(
                        Calendar.MINUTE),true)
                    timePickerDialog.setOnCancelListener {
                        Toast.makeText(
                            context,
                            "A data foi não pode ser definida.",
                            Toast.LENGTH_SHORT
                        ).show()   //shows the toast if input field is empty
                    }
                    timePickerDialog.setCancelable(false)
                    timePickerDialog.show()
                }
            }
        }

        override fun getItemCount(): Int {
            return spinner.selectedItem.toString().toInt()
        }
    }
}