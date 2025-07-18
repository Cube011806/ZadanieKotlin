package com.kk.zadaniekotlin.ui.home

import SharedViewModel
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.HomeViewModel
import com.kk.zadaniekotlin.databinding.FragmentHomeBinding
import com.kk.zadaniekotlin.ui.home.HomeUiState

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val buttons by lazy {
        listOf(
            binding.imageButton1,
            binding.imageButton2,
            binding.imageButton3,
            binding.imageButton4,
            binding.imageButton5
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is HomeUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    state.imageUrls.zip(buttons).forEach { (url, button) ->
                        Glide.with(requireContext())
                            .load(url)
                            .placeholder(ColorDrawable(Color.WHITE))
                            .error(ColorDrawable(Color.RED))
                            .into(button)
                    }
                }
                is HomeUiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Brak obrazów do wyświetlenia", Toast.LENGTH_SHORT).show()
                }
                is HomeUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Błąd: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

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
                sharedViewModel.setCatId(pair.first)
                val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(pair.first)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
