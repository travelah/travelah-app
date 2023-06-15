package com.travelah.travelahapp.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.travelah.travelahapp.databinding.FragmentProfileBinding
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.editprofile.EditProfileActivity
import com.travelah.travelahapp.view.main.MainViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val profileViewModel: ProfileViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = requireActivity().let { ViewModelFactory.getInstance(it) }!!
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        binding.buttonEdit.setOnClickListener {
            mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
                val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                intent.putExtra(EditProfileActivity.EXTRA_TOKEN, token)
                startActivity(intent)
            }
        }
    }

    private fun setupView() {
        profileViewModel.getProfile().observe(viewLifecycleOwner) {
            binding.apply {
                textName.text = it.fullName
                textAboutMe.text = it.aboutMe
                textAge.text = it.age.toString()
                textOccupation.text = it.occupation
                textLocation.text = it.location
            }
            Glide.with(requireActivity())
                .load(it.photo)
                .into(binding.imageProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}