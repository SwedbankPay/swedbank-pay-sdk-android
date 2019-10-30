package com.swedbankpay.exampleapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer

class PaymentErrorDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().mainViewModel.currentErrorMessage.observe(this, Observer {
            if (it != null) {
                (dialog as? AlertDialog)?.setMessage(it)
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel = requireActivity().mainViewModel
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_dialog_title)
            .setMessage(viewModel.currentErrorMessage.value?.takeUnless { it.isEmpty() })
            .setNeutralButton(R.string.close) { _, _ ->
                viewModel.clearErrorMessage()
                dismiss()
            }
            .create()
    }
}