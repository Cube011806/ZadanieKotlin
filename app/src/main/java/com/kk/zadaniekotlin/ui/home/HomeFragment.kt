package com.kk.zadaniekotlin.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
            "https://rownowazni.trefl.com/wp-content/uploads/2022/09/Projekt-bez-tytulu-75-600x600.jpg",
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
        val clickAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)



        binding.imageButton1.setOnClickListener {
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Kobiety", Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.navigation_category)
        }
        binding.imageButton2.setOnClickListener {
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Mężczyzna",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.navigation_category)
        }
        binding.imageButton3.setOnClickListener {
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Niemowlak",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.navigation_category)
        }
        binding.imageButton4.setOnClickListener {
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Dziewczynka",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.navigation_category)
        }
        binding.imageButton5.setOnClickListener() {
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Chłopiec",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.navigation_category)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}