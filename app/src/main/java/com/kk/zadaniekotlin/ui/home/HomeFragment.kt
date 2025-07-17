package com.kk.zadaniekotlin.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.HomeViewModel
import com.kk.zadaniekotlin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val buttons = listOf(
            binding.imageButton1,
            binding.imageButton2,
            binding.imageButton3,
            binding.imageButton4,
            binding.imageButton5
        )

        // Obserwacja URL-i obrazków
        homeViewModel.imageUrls.observe(viewLifecycleOwner) { urlList ->
            urlList.zip(buttons).forEach { (url, button) ->
                Glide.with(requireContext())
                    .load(url)
                    .placeholder(ColorDrawable(Color.WHITE))
                    .error(ColorDrawable(Color.RED))
                    .into(button)
            }
        }

        // Obsługa kliknięć
        val clickMap = mapOf(
            binding.imageButton1 to Pair(1, "Kobiety"),
            binding.imageButton2 to Pair(2, "Mężczyzna"),
            binding.imageButton3 to Pair(3, "Niemowlak"),
            binding.imageButton4 to Pair(4, "Dziewczynka"),
            binding.imageButton5 to Pair(5, "Chłopiec")
        )

        clickMap.forEach { (button, pair) ->
            button.setOnClickListener {
                Toast.makeText(requireContext(), "Kliknięto: ${pair.second}", Toast.LENGTH_SHORT).show()
                val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(pair.first)
                findNavController().navigate(action)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
