package com.kk.zadaniekotlin.ui.basket

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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

    private val basketViewModel: BasketViewModel by activityViewModels { viewModelFactory }

    private lateinit var adapter: BasketItemAdapter

    private var listState: Parcelable? = null

    override fun onAttach(context: Context) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            showNotLoggedInMessage()
            return
        }

        setupRecyclerView()
        observeViewModel()
        basketViewModel.loadCartFromFirebase()
    }

    private fun setupRecyclerView() {
        adapter = BasketItemAdapter { item ->
            basketViewModel.removeItem(item)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        basketViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)

            if (listState != null) {
                binding.recyclerView.layoutManager?.onRestoreInstanceState(listState)
                listState = null
            }

            binding.emptyTextView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
        }

        basketViewModel.cartSum.observe(viewLifecycleOwner) { sum ->
            binding.sumView.text = getString(R.string.sum_price, sum)
        }

        basketViewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state is BasketUiState.Loading) View.VISIBLE else View.GONE
        }
    }

    private fun showNotLoggedInMessage() {
        with(binding) {
            recyclerView.visibility = View.GONE
            emptyTextView.text = getString(R.string.basket_notLoggedIn)
            emptyTextView.visibility = View.VISIBLE
            textView3.visibility = View.GONE
            progressBar.visibility = View.GONE
            button2.visibility = View.GONE
            sumView.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        listState = binding.recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
