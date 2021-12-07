package com.swedbankpay.mobilesdk.internal

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.swedbankpay.mobilesdk.R

internal class WebViewFragment : Fragment() {
    private companion object {
        private fun wrangleBottomSheet(
            bottomSheetBehavior: BottomSheetBehavior<*>,
            bottomSheetContainer: View,
            onBackPressedCallback: OnBackPressedCallback
        ) {
            val isOpen = when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> false
                else -> true
            }
            bottomSheetContainer.visibility = when (isOpen) {
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
            onBackPressedCallback.isEnabled = isOpen
        }
    }

    private val internalPaymentViewModelProvider get() = ViewModelProvider(requireParentFragment())

    private val internalPaymentViewModel get() = internalPaymentViewModelProvider
        .get<InternalPaymentViewModel>()

    private val webViewModel get() = ViewModelProvider(this).get<WebViewModel>()

    private var restored = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webViewModel.javascriptDialogTags.observe(this, {
            ensureJSDialogFragments(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        restored = savedInstanceState != null

        return inflater.inflate(R.layout.swedbankpaysdk_web_view_fragment, container, false).apply {
            findViewById<FrameLayout>(R.id.swedbankpaysdk_web_view_container)
                .addView(getRootWebView(inflater.context))

            setupExtraWebView(
                bottomSheetContainer = findViewById(R.id.swedbankpaysdk_web_view_overlay_holder),
                bottomSheet = findViewById(R.id.swedbankpaysdk_web_view_overlay),
                extraWebViewToolbar = findViewById(R.id.swedbankpaysdk_extra_web_view_toolbar),
                extraWebViewContainer = findViewById(R.id.swedbankpaysdk_extra_web_view_container)
            )
        }
    }

    private fun getRootWebView(context: Context): WebView {
        return webViewModel.apply {
            setup(context, internalPaymentViewModelProvider)
        }.requireWebView().apply {
            internalPaymentViewModel.updatingPaymentOrder.observe(viewLifecycleOwner) {
                Log.d(LOG_TAG, "WebView enabled ${it != true}")
                isEnabled = it != true
            }
        }
    }

    private fun setupExtraWebView(
        bottomSheetContainer: View,
        bottomSheet: View,
        extraWebViewToolbar: Toolbar,
        extraWebViewContainer: FrameLayout
    ) {
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        val onBackPressed = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                wrangleBottomSheet(bottomSheetBehavior, bottomSheetContainer, this)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)

        extraWebViewToolbar.setNavigationOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            wrangleBottomSheet(bottomSheetBehavior, bottomSheetContainer, onBackPressed)
        }

        observeExtraWebView(
            bottomSheetBehavior = bottomSheetBehavior,
            bottomSheetContainer = bottomSheetContainer,
            extraWebViewContainer = extraWebViewContainer,
            onBackPressedCallback = onBackPressed
        )

        observeBottomSheetState(
            bottomSheetBehavior = bottomSheetBehavior,
            bottomSheetContainer = bottomSheetContainer,
            onBackPressedCallback = onBackPressed
        )

        wrangleBottomSheet(bottomSheetBehavior, bottomSheetContainer, onBackPressed)
    }

    private fun observeExtraWebView(
        bottomSheetBehavior: BottomSheetBehavior<*>,
        bottomSheetContainer: View,
        extraWebViewContainer: FrameLayout,
        onBackPressedCallback: OnBackPressedCallback
    ) {
        webViewModel.extraWebView.observe(viewLifecycleOwner) {
            if (it == null) {
                extraWebViewContainer.removeAllViews()
            } else {
                if (it != extraWebViewContainer.getChildAt(0)) {
                    extraWebViewContainer.removeAllViews()
                    extraWebViewContainer.addView(it)
                }
            }
            bottomSheetBehavior.state = when (it) {
                null -> BottomSheetBehavior.STATE_HIDDEN
                else -> BottomSheetBehavior.STATE_EXPANDED
            }
            wrangleBottomSheet(bottomSheetBehavior, bottomSheetContainer, onBackPressedCallback)
        }
    }

    private fun observeBottomSheetState(
        bottomSheetBehavior: BottomSheetBehavior<*>,
        bottomSheetContainer: View,
        onBackPressedCallback: OnBackPressedCallback
    ) {
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                wrangleBottomSheet(bottomSheetBehavior, bottomSheetContainer, onBackPressedCallback)
                if (newState == BottomSheetBehavior.STATE_COLLAPSED
                    || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    webViewModel.removeExtraWebView()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do nothing
            }
        })
    }

    override fun onPause() {
        super.onPause()
        webViewModel.onPause()
    }

    override fun onResume() {
        webViewModel.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        view?.apply {
            // The WebViews are part of the view model, and will be reused if the view
            // or this fragment is recreated. Therefore, we must explicitly remove them
            // from their parents at this point.
            findViewById<FrameLayout>(R.id.swedbankpaysdk_web_view_container).removeAllViews()
            findViewById<FrameLayout>(R.id.swedbankpaysdk_extra_web_view_container).removeAllViews()
        }
        super.onDestroyView()
    }

    fun clear() {
        restored = false
        webViewModel.clearContent()
    }

    fun load(baseUrl: String?, htmlString: String): Boolean {
        val vm = webViewModel
        if (restored) {
            restored = false
            // we were restored, so do not (re)load the page if it is the current root page
            if (vm.isShowingContentRootedAt(htmlString)) return false
        }
        vm.loadContent(baseUrl, htmlString)
        return true
    }

    private fun ensureJSDialogFragments(tags: Set<String>) {
        val manager = childFragmentManager

        val tagsToShow = tags.toMutableSet()
        for (fragment in manager.fragments) {
            if (fragment is JSDialogFragment && !tagsToShow.remove(fragment.tag)) {
                fragment.dismiss()
            }
        }
        // at this point, tagsToShow contains any tags for which we do not yet have a JSDialogFragment
        for (tag in tagsToShow) {
            JSDialogFragment().show(manager, tag)
        }
    }

    internal class JSDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

        private val webViewModel get() = (parentFragment as? WebViewFragment)?.webViewModel

        private var resultSent = false

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(requireContext(), theme)
                .also {
                    buildDialog(it)
                    it.setPositiveButton(android.R.string.ok, this)
                }
                .create()
        }

        private fun setNegativeButton(builder: AlertDialog.Builder) {
            builder.setNegativeButton(android.R.string.cancel, this)
        }

        private fun buildDialog(builder: AlertDialog.Builder) {
            when (val info = tag?.let { webViewModel?.getDialogInfo(it) }) {
                is WebViewModel.JSDialogInfo.Alert -> builder.setMessage(info.message)
                is WebViewModel.JSDialogInfo.Confirm -> {
                    builder.setMessage(info.message)
                    setNegativeButton(builder)
                }
                is WebViewModel.JSDialogInfo.Prompt -> {
                    @SuppressLint("InflateParams") // must use null as parent of dialog view
                    val view = LayoutInflater.from(builder.context).inflate(R.layout.swedbankpaysdk_webviewfragment_prompt, null)
                    view.findViewById<TextView>(R.id.swedbankpaysdk_prompt_message).text = info.message
                    view.findViewById<EditText>(R.id.swedbankpaysdk_prompt_value).setText(info.defaultValue)
                    builder.setView(view)
                    setNegativeButton(builder)
                }
                null -> Unit
            }
        }

        private inline fun sendResult(f: (WebViewModel.JSDialogInfo<*>) -> Unit) {
            if (!resultSent) {
                resultSent = true
                tag?.let {
                    webViewModel?.consumeDialogInfo(it)?.let(f)
                }
            }
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
            sendResult {
                when (it) {
                    is WebViewModel.JSDialogInfo.Alert -> it.result.confirm()
                    is WebViewModel.JSDialogInfo.Confirm -> it.result.cancel()
                    is WebViewModel.JSDialogInfo.Prompt -> it.result.cancel()
                }
            }
        }

        override fun onClick(dialog: DialogInterface?, which: Int) {
            sendResult {
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE -> it.result.cancel()
                    DialogInterface.BUTTON_POSITIVE -> when (it) {
                        is WebViewModel.JSDialogInfo.Alert -> it.result.confirm()
                        is WebViewModel.JSDialogInfo.Confirm -> it.result.confirm()
                        is WebViewModel.JSDialogInfo.Prompt -> {
                            val value = this.dialog?.findViewById<EditText>(R.id.swedbankpaysdk_prompt_value)?.text?.toString()
                            it.result.confirm(value ?: "")
                        }
                    }
                }

            }
        }
    }
}
