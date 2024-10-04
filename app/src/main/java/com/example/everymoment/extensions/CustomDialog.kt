package com.example.everymoment.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.everymoment.databinding.DialogCustomBinding

class CustomDialog(
    private val message: String,
    private val negText: String,
    private val posText: String,
    private val onNegativeClick: (() -> Unit)? = null,
    private val onPositiveClick: (() -> Unit)? = null
) : DialogFragment() {

    private lateinit var binding: DialogCustomBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setFlags(
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND
        )

        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCustomBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.dialogMessage.text = message
        binding.negButton.text = negText
        binding.posButton.text = posText

        binding.negButton.setOnClickListener {
            onNegativeClick?.invoke()
            dismiss()
        }

        binding.posButton.setOnClickListener {
            onPositiveClick?.invoke()
            dismiss()
        }

        return view
    }


}