package com.kk.zadaniekotlin.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.databinding.FragmentHomeBinding
import com.kk.zadaniekotlin.R
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val urls = listOf(
            "https://img.freepik.com/darmowe-zdjecie/widok-z-boku-usmiechnietej-kobiety-kawie-w-lozku_23-2148832943.jpg?semt=ais_hybrid&w=740",
            "https://patryktarachon.pl/wp-content/uploads/2020/01/Sklep-Ozonee-ubrania-dla-m%C4%99%C5%BCczyzn-Patryk-Taracho%C5%84-2020-scaled.jpg",
            "https://supermama.edu.pl/wp-content/uploads/2024/04/blog-zdjecia-pionowe-1-1-683x1024-1.png",
            "https://static.kappahl.com/cdn-cgi/image/width=768,format=auto/globalassets/productimages/464230_f.jpg?ref=F89B866770",
            "https://mokida.com/media/catalog/product/W/M/WM4143305POB_001_01_5fbc.jpg?store=default&image-type=small_image&auto=webp&format=pjpg&width=538&height=806&fit=cover"
        )

        val buttons = listOf(
            binding.imageButton1,
            binding.imageButton2,
            binding.imageButton3,
            binding.imageButton4,
            binding.imageButton5
        )

        urls.zip(buttons).forEach { (url, button) ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(ColorDrawable(Color.WHITE))
                .error(ColorDrawable(Color.RED))
                .into(button)
        }

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}