package com.example.everymoment.data.model.network.dto.request


import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class PatchFilesRequest(
    @SerializedName("filename")
    val files: List<MultipartBody.Part>
)