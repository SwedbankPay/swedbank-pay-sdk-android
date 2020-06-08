package com.swedbankpay.mobilesdk.internal

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.swedbankpay.mobilesdk.R
import java.net.URISyntaxException

internal class WebViewFragment : Fragment() {
    private companion object {
        private val INTENT_URI_FLAGS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent.URI_ANDROID_APP_SCHEME or Intent.URI_INTENT_SCHEME
        } else {
            Intent.URI_INTENT_SCHEME
        }
    }

    private val webViewModel get() = ViewModelProvider(this).get<WebViewModel>()

    private var restored = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webViewModel.failedIntentUris.observe(this, Observer {
            if (it?.isNotEmpty() == true) {
                for (uri in it) {
                    showIntentUriErrorDialog(uri)
                }
                webViewModel.failedIntentUris.value = null
            }
        })
        webViewModel.javascriptDialogTags.observe(this, Observer {
            ensureJSDialogFragments(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        restored = savedInstanceState != null

        return webViewModel.apply {
            setup(inflater.context, ViewModelProvider(requireParentFragment()))
        }.requireWebView()
    }

    override fun onPause() {
        super.onPause()
        webViewModel.requireWebView().onPause()
    }

    override fun onResume() {
        webViewModel.requireWebView().onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        webViewModel.apply {
            requireWebView().let {
                (it.parent as? ViewManager)?.removeView(it)
            }
        }
        super.onDestroyView()
    }

    fun clear() {
        restored = false
        webViewModel.clearContent()
    }

    fun load(baseUrl: String, htmlString: String): Boolean {
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

    private fun showIntentUriErrorDialog(uri: Uri) {
        ErrorDialogFragment.newInstance(uri).show(childFragmentManager, null)
    }

    class JSDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

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

    class ErrorDialogFragment : DialogFragment() {
        companion object {
            const val ARG_URI = "U"

            fun newInstance(uri: Uri) = ErrorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri.toString())
                }
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = requireContext()
            return AlertDialog.Builder(context, theme)
                .setTitle(R.string.swedbankpaysdk_intent_failure_title)
                .setMessage(context.formatMessage())
                .setNeutralButton(R.string.swedbankpaysdk_intent_failure_close) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        }

        private fun Context.formatMessage(): String {
            val packageName = getFailingPackage() ?: getString(R.string.swedbankpaysdk_intent_failure_unknown)
            return getString(R.string.swedbankpaysdk_intent_failure_body, packageName)
        }


        private fun getFailingPackage(): String? {
            val uri = arguments?.getString(ARG_URI)
            val intent = try {
                uri?.let { Intent.parseUri(it, INTENT_URI_FLAGS) }
            } catch (_: URISyntaxException) {
                null
            }
            return intent?.`package`
        }
    }
}
