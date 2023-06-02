package com.travelah.travelahapp.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.travelah.travelahapp.databinding.FragmentHomeBinding
import com.travelah.travelahapp.ui.screens.HomeScreen
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val postViewModel: PostViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            postViewModel.getMostLikedPost(token).observe(viewLifecycleOwner) { result ->
                binding.composeView.apply {
                    // Dispose of the Composition when the view's LifecycleOwner
                    // is destroyed
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        // In Compose world
                        MaterialTheme {
                            HomeScreen(result)
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}