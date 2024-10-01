package com.example.everymoment.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentSettingBinding
import com.example.everymoment.extensions.GalleryUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    private val galleryUtil = GalleryUtil(this) { imageUri ->
        addImage(imageUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountImage.setOnClickListener {
            showProfileImageDialog()
        }

        binding.cameraButton.setOnClickListener {
            showProfileImageDialog()
        }

        binding.editButton.setOnClickListener {
            showProfileNameDialog()
        }
    }

    private fun showProfileImageDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.WhiteDialog)
            .setMessage(resources.getString(R.string.profile_image_dialog))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.profile_image_dialog_posiitve)) { dialog, _ ->
                galleryUtil.openGallery()
                dialog.dismiss()
            }
            .show()
    }

    private fun showProfileNameDialog() {
    }

    private fun addImage(imageUri: Uri?) {
        Glide.with(this)
            .load(imageUri)
            .circleCrop()
            .into(binding.accountImage)
    }
}