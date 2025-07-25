package com.kk.zadaniekotlin.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.MyApplication
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.databinding.FragmentHomeBinding
import javax.inject.Inject
import androidx.core.graphics.drawable.toDrawable

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        super.onAttach(context)
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
            binding.progressBar.visibility = if (state is HomeUiState.Loading) View.VISIBLE else View.GONE
            when (state) {
                is HomeUiState.Loading -> {binding.progressBar.visibility = View.VISIBLE}
                is HomeUiState.Success -> loadCategoryImages(state.categoryImages)
                is HomeUiState.Empty -> Toast.makeText(requireContext(), "Brak obrazów do wyświetlenia", Toast.LENGTH_SHORT).show()
                is HomeUiState.Error -> Toast.makeText(requireContext(), "Błąd: ${state.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getButtons(): List<ImageButton> = listOf(
        binding.imageButton1,
        binding.imageButton2,
        binding.imageButton3,
        binding.imageButton4,
        binding.imageButton5
    )

    private fun loadCategoryImages(images: List<CategoryImage>) {
        images.zip(getButtons()).forEach { (category, button) ->
            Glide.with(requireContext())
                .load(category.imageUrl)
                .placeholder(Color.WHITE.toDrawable())
                .error(Color.RED.toDrawable())
                .into(button)

            button.setOnClickListener {
                Toast.makeText(requireContext(), "Kliknięto: ${category.name}", Toast.LENGTH_SHORT).show()
                homeViewModel.onCategoryClicked(category.id)
                sharedViewModel.setCatId(category.id)
                val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(category.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
