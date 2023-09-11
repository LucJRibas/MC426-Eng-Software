//package com.project.lembretio
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.project.lembretio.databinding.FragmentFirstBinding
//
//
///**
// * A simple [Fragment] subclass as the default destination in the navigation.
// */
//class FirstFragment : Fragment() {
//
//    private var _binding: FragmentFirstBinding? = null
//    private lateinit var eventAdapter: EventAdapter
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        _binding = FragmentFirstBinding.inflate(inflater, container, false)
//
//        binding.rvEventActivity.layoutManager = LinearLayoutManager(context)
//        eventAdapter = EventAdapter(mutableListOf(
//            Event("testes", false),
//            Event("testes2", false)))
//        binding.rvEventActivity.adapter = eventAdapter
//
//        binding.btnCreateEvent.setOnClickListener {
//            val intent = Intent(context, EventActivity::class.java)
//            startActivity(intent)
//        }
//
//        val eventTitle = activity?.intent?.getStringExtra("asd")
//        Log.d("t1", "title: $eventTitle")
//
//        eventTitle?.let {
//            Log.d("t1", "tasdfas")
//            eventAdapter.addEvent(Event(it, false))
//        }
//
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}