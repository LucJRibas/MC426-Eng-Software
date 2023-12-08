package com.project.lembretio

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment;

class EventPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> EventDate()
            else -> EventTitle()
        }
    }
    override fun getItemCount(): Int {
        return 2
    }
}