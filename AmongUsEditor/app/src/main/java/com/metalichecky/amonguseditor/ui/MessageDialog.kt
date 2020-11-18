package com.metalichecky.amonguseditor.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.util.TypefaceUtils
import com.metalichecky.amonguseditor.util.setCustomTypeface
import kotlinx.android.synthetic.main.dialog_message.*
import timber.log.Timber

class MessageDialog(
    val title: String?,
    val message: String?,
    val listener: Listener? = null
) : DialogFragment() {
    companion object {
        const val TAG = "MessageDialog"
    }

    private var rootView: View? = null

    override fun getView(): View? {
        // kotlinx.android.synthetic fields won't work without it
        return rootView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_message, null)
        val builder = AlertDialog.Builder(
            requireActivity(),
            R.style.MessageDialogStyle
        ).setView(view)
        rootView = view
        val dialog = builder.create()


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnOk.setCustomTypeface(TypefaceUtils.TypeFaces.SKINNY_BOLD)
        tvTitle.setCustomTypeface(TypefaceUtils.TypeFaces.SKINNY_BOLD)
        tvMessage.setCustomTypeface(TypefaceUtils.TypeFaces.OSWALD_REGULAR)

        title?.let {
            tvTitle?.setText(it)
        } ?: tvTitle?.setVisibility(View.GONE)
        message?.let {
            tvMessage?.setText(it)
        } ?: tvMessage?.setVisibility(View.GONE)

        btnOk.setOnClickListener {
            dismiss()
        }

        return dialog
    }



    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Timber.d("dismissed")
        listener?.onClosed()
    }

    interface Listener {
        fun onClosed()
    }
}