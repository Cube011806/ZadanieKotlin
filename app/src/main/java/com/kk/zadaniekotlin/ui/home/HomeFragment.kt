package com.kk.zadaniekotlin.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.kk.zadaniekotlin.databinding.FragmentHomeBinding
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.model.ItemModelImpl

class HomeFragment : Fragment() {
    val database = FirebaseDatabase.getInstance()
    val urlRef = database.getReference("imageUrls")
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
        loadImageUrls()
        binding.imageButton1.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(1)
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Kobiety", Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(action)
        }
        binding.imageButton2.setOnClickListener {
            //it.startAnimation(clickAnim)
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(2)
            Toast.makeText(
                root.context,
                "Kliknięto: Mężczyzna",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(action)
        }
        binding.imageButton3.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(3)
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Niemowlak",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(action)
        }
        binding.imageButton4.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(4)
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Dziewczynka",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(action)
        }
        binding.imageButton5.setOnClickListener() {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCategory(5)
            //it.startAnimation(clickAnim)
            Toast.makeText(
                root.context,
                "Kliknięto: Chłopiec",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(action)
        }
        return root
    }
    fun loadImageUrls() {
        val database = FirebaseDatabase.getInstance()
        val urlRef = database.getReference("imageUrls")

        urlRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val urlList = mutableListOf<String>()
                for (child in snapshot.children) {
                    val url = child.getValue(String::class.java)
                    url?.let { urlList.add(it) }
                }

                val buttons = listOf(
                    binding.imageButton1,
                    binding.imageButton2,
                    binding.imageButton3,
                    binding.imageButton4,
                    binding.imageButton5
                )

                urlList.zip(buttons).forEach { (url, button) ->
                    Glide.with(requireContext())
                        .load(url)
                        .placeholder(ColorDrawable(Color.WHITE))
                        .error(ColorDrawable(Color.RED))
                        .into(button)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Błąd ładowania URL-i: ${error.message}")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}