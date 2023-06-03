package com.travelah.travelahapp.view.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.travelah.travelahapp.R
import com.travelah.travelahapp.databinding.FragmentPostContentBinding

class PostContentFragment : Fragment() {

    private var _binding: FragmentPostContentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        val text = root.findViewById<TextView>(R.id.hello)
        text.text = index.toString()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}