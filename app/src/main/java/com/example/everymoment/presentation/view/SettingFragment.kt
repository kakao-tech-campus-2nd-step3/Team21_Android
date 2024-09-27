package com.example.everymoment.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.databinding.FragmentDiaryEditBinding
import com.example.everymoment.databinding.FragmentSettingBinding
import com.example.everymoment.extensions.GalleryUtil

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
            galleryUtil.openGallery()
        }

        binding.cameraButton.setOnClickListener {
            galleryUtil.openGallery()
        }

        binding.editButton.setOnClickListener {

        }
    }

    private fun addImage(imageUri: Uri?) {
        Glide.with(this)
            .load(imageUri)
            .circleCrop()
            .into(binding.accountImage)
    }
}