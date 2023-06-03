package com.travelah.travelahapp.view.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.paging.compose.collectAsLazyPagingItems
import com.travelah.travelahapp.databinding.FragmentPostContentBinding
import com.travelah.travelahapp.ui.screens.PostScreen
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel

class PostContentFragment : Fragment() {

    private var _binding: FragmentPostContentBinding? = null

    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val postViewModel: PostViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!
        _binding = FragmentPostContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token != "") {
                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        // In Compose world
                        MaterialTheme {
                            val posts = postViewModel.getAllPost(token, index == 2).collectAsLazyPagingItems()
                            PostScreen(
                                posts
                            )
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

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}