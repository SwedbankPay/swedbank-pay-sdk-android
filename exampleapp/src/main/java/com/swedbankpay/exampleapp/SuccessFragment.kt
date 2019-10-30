package com.swedbankpay.exampleapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SuccessFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.success_dialog_title)
            .setNeutralButton(R.string.close) { _, _ ->
                dismiss()
            }
            .create()
    }
}