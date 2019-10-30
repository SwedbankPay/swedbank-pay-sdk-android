package com.swedbankpay.exampleapp.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.swedbankpay.exampleapp.R
import kotlinx.android.synthetic.main.fragment_products.view.*

class ProductsFragment : Fragment(R.layout.fragment_products) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // N.B! Add observers in onCreate, not onCreateView/onViewCreated.
        // This way they only get added once, even if the view
        // is destroyed and recreated.
        val viewModel = requireActivity().productsViewModel

        viewModel.onCloseCartPressed.observe(this, Observer {
            if (it != null) childFragmentManager.popBackStack()
        })

        viewModel.onCheckOutPressed.observe(this, Observer {
            if (it != null) {
                navigateToPayment()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.apply {
                val cartAndSettingsFragment =
                    checkNotNull(findFragmentById(R.id.cart_and_settings))

                beginTransaction()
                    .hide(cartAndSettingsFragment)
                    .commit()
            }
        }

        view.recyclerView.adapter = ProductsAdapter(
            this,
            requireActivity().productsViewModel
        )

        view.open_cart.setOnClickListener {
            childFragmentManager.apply {
                val cartAndSettingsFragment =
                    checkNotNull(findFragmentById(R.id.cart_and_settings))

                if (cartAndSettingsFragment.isHidden) {
                    beginTransaction()
                        .addToBackStack(null)
                        .show(cartAndSettingsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
            }
        }
    }

    private fun navigateToPayment() {
        // A MediatorLiveData only updates when it has an active observer
        val arguments = requireActivity().productsViewModel.paymentFragmentArguments.run {
            if (!hasActiveObservers()) {
                val observer = Observer<Any> {}
                observeForever(observer)
                removeObserver(observer)
            }
            value
        }
        if (arguments != null) {
            findNavController().navigate(R.id.action_productsFragment_to_paymentFragment, arguments)
        }
    }
}