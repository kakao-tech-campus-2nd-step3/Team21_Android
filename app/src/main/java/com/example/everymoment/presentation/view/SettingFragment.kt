package com.example.everymoment.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentSettingBinding
import com.example.everymoment.extensions.CustomDialog
import com.example.everymoment.extensions.CustomEditDialog
import com.example.everymoment.extensions.GalleryUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    private val galleryUtil = GalleryUtil(this)

    private lateinit var profileImageDialog: CustomDialog
    private lateinit var profileNameDialog: CustomDialog
    private lateinit var nameChangeDialog: CustomEditDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDialogs()

        binding.accountImage.setOnClickListener {
            profileImageDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.cameraButton.setOnClickListener {
            profileImageDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.editButton.setOnClickListener {
            profileNameDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }
        
        binding.notificationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), resources.getString(R.string.notification_isChecked), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.notification_isUnChecked), Toast.LENGTH_SHORT).show()
            }
        }

        binding.timeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            var selectedTime = ""
            when (checkedId) {
                R.id.time15m -> {
                    selectedTime = resources.getString(R.string.time_15min)
                }
                R.id.time20m -> {
                    selectedTime = resources.getString(R.string.time_20min)
                }
                R.id.time25m -> {
                    selectedTime = resources.getString(R.string.time_25min)
                }
                R.id.time30m -> {
                    selectedTime = resources.getString(R.string.time_30min)
                }
            }
            Toast.makeText(requireContext(), getString(R.string.time_interval_text, selectedTime), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDialogs() {
        profileImageDialog =
            CustomDialog(
                resources.getString(R.string.profile_image_dialog),
                resources.getString(R.string.cancel),
                resources.getString(R.string.change),
                onPositiveClick = {
                    galleryUtil.openGallery(onImageSelected = {
                        addImage(it)
                    })
                }).apply {
                isCancelable = false
            }

        profileNameDialog =
            CustomDialog(
                resources.getString(R.string.profile_name_dialog),
                resources.getString(R.string.cancel),
                resources.getString(R.string.change),
                onPositiveClick = {
                    nameChangeDialog.show(
                        requireActivity().supportFragmentManager,
                        "CustomEditDialog"
                    )
                }).apply {
                isCancelable = false
            }

        nameChangeDialog =
            CustomEditDialog(
                resources.getString(R.string.change_name_dialog),
                resources.getString(R.string.example_name),
                resources.getString(R.string.change_name_dialog_instruction),
                resources.getString(R.string.cancel),
                resources.getString(R.string.save),
                onPositiveClick = {
                    binding.accountName.text = it
                })
    }

    private fun addImage(imageUri: Uri?) {
        Glide.with(this)
            .load(imageUri)
            .circleCrop()
            .into(binding.accountImage)
    }
}