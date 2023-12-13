package com.project.lembretio

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment;
import com.project.lembretio.addeventfragments.EventAlarm
import com.project.lembretio.addeventfragments.EventComment
import com.project.lembretio.addeventfragments.EventDate
import com.project.lembretio.addeventfragments.EventFrequency
import com.project.lembretio.addeventfragments.EventMany
import com.project.lembretio.addeventfragments.EventTitle

class MedicationPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> EventFrequency()
            2 -> EventMany()
            3 -> EventDate()
            4 -> EventComment()
            5 -> EventAlarm()
            else -> EventTitle()
        }
    }
    override fun getItemCount(): Int {
        return 6
    }
}