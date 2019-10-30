package com.swedbankpay.exampleapp.cartsettings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.swedbankpay.exampleapp.R
import com.swedbankpay.exampleapp.products.productsViewModel
import kotlinx.android.synthetic.main.fragment_cart_and_settings.*

class CartAndSettingsFragment : Fragment(R.layout.fragment_cart_and_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = CartAndSettingsAdapter(
            this,
            requireActivity().productsViewModel
        )
        recyclerView.addItemDecoration(CartAndSettingsItemDecoration(requireContext()))
    }
}