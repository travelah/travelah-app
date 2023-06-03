package com.travelah.travelahapp.view.post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PostSectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment? = PostContentFragment()
        fragment?.arguments = Bundle().apply {
            putInt(PostContentFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}