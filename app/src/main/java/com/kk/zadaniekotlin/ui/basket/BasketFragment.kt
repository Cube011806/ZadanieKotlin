package com.kk.zadaniekotlin.ui.basket

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.kk.zadaniekotlin.MyApplication
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.databinding.FragmentBasketBinding
import javax.inject.Inject

class BasketFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private val basketViewModel: BasketViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var adapter: BasketItemAdapter

    override fun onAttach(context: Context) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyTextView.text = getString(R.string.basket_notLoggedIn)
            binding.textView3.visibility = View.GONE
            binding.emptyTextView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.button2.visibility = View.GONE
            binding.sumView.visibility = View.GONE
            return
        }

        adapter = BasketItemAdapter(mutableListOf()) { item ->
            basketViewModel.removeItem(item)
            basketViewModel.saveCartToFirebase()
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = adapter

        basketViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateData(items)
            binding.emptyTextView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
        }

        basketViewModel.cartSum.observe(viewLifecycleOwner) { sum ->
            binding.sumView.text = context?.getString(R.string.sum_price, sum)
        }

        basketViewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state is BasketUiState.Loading) View.VISIBLE else View.GONE
        }

        basketViewModel.cartItems.observe(viewLifecycleOwner) {
            basketViewModel.saveCartToFirebase()
        }

        basketViewModel.loadCartFromFirebase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
