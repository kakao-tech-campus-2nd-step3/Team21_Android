package com.example.everymoment.data.model.network.dto.request


import com.example.everymoment.data.model.network.dto.vo.File
import com.google.gson.annotations.SerializedName

data class PostFilesRequest(
    @SerializedName("filename")
    val files: List<File>
)