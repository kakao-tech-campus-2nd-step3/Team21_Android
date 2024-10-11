package com.example.everymoment.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class GalleryUtil(
    private val fragment: Fragment
) {

    private var listener: ((Uri?) -> Unit)? = null

    private val galleryLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            listener?.invoke(imageUri)
        }
    }

    fun openGallery(onImageSelected: (Uri?) -> Unit) {
        this.listener = onImageSelected
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

}