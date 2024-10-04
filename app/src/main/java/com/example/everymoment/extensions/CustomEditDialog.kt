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
import com.example.everymoment.databinding.DialogCustomEditBinding

class CustomEditDialog(
    private val message: String,
    private val editText: String = "",
    private val editHint: String = "",
    private val instruction: String = "",
    private val negText: String,
    private val posText: String,
    private val onNegativeClick: (() -> Unit)? = null,
    private val onPositiveClick: ((String) -> Unit)? = null,
    private val removeEditText: Boolean = false
) : DialogFragment() {

    private lateinit var binding: DialogCustomEditBinding

    fun setWrongInstruction(instruction: String) {
        binding.instruction.setText(instruction)
        binding.instruction.setTextColor(Color.RED)
    }

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
        binding = DialogCustomEditBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.dialogMessage.text = message
        binding.editText.setText(editText)
        binding.editText.hint = editHint
        binding.instruction.text = instruction
        binding.negButton.text = negText
        binding.posButton.text = posText

        binding.negButton.setOnClickListener {
            onNegativeClick?.invoke()
            dismiss()
        }

        binding.posButton.setOnClickListener {
            onPositiveClick?.invoke(binding.editText.text.toString())
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        if (removeEditText)
            binding.editText.setText("")
    }

}