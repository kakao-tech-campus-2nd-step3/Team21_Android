package potatocake.katecam.everymoment.extensions

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SendFilesUtil {

    companion object {
        private val client = OkHttpClient()

        private fun downloadImageAsync(
            url: String,
            context: Context,
            callback: (File?) -> Unit
        ) {
            val tempFile = File.createTempFile("temp_image", null, context.cacheDir)

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful || response.body == null) {
                        callback(null)
                        return
                    }

                    response.body!!.byteStream().use { inputStream ->
                        FileOutputStream(tempFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    callback(tempFile)
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("SendFilesUtil", "Image download failed: ${e.message}")
                    callback(null)
                }
            })
        }


        fun uriToFile(
            context: Context,
            imagesList: List<String>,
            callback: (List<MultipartBody.Part>) -> Unit
        ) {
            if (imagesList.isEmpty()) {
                callback(emptyList())
                return
            }
            val parts = mutableListOf<MultipartBody.Part>()
            val remainingImages = imagesList.toMutableList()

            processNext(context, remainingImages, parts, callback)
        }


        private fun processNext(
            context: Context,
            remainingImages: MutableList<String>,
            parts: MutableList<MultipartBody.Part>,
            callback: (List<MultipartBody.Part>) -> Unit
        ) {
            if (remainingImages.isEmpty()) {
                callback(parts)
                return
            }

            val imagePath = remainingImages.removeAt(0)
            val uri = Uri.parse(imagePath)

            when (uri.scheme) {
                "content" -> {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val tempFile = File.createTempFile("temp_image", null, context.cacheDir)
                    inputStream?.use { input ->
                        FileOutputStream(tempFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    createMultipartBodyPart(tempFile, parts) {
                        processNext(
                            context,
                            remainingImages,
                            parts,
                            callback
                        )
                    }
                }

                "file" -> {
                    val file = File(uri.path ?: "")
                    createMultipartBodyPart(file, parts) {
                        processNext(
                            context,
                            remainingImages,
                            parts,
                            callback
                        )
                    }
                }

                "https" -> {
                    downloadImageAsync(uri.toString(), context) { downloadedFile ->
                        if (downloadedFile != null) {
                            createMultipartBodyPart(downloadedFile, parts) {
                                processNext(
                                    context,
                                    remainingImages,
                                    parts,
                                    callback
                                )
                            }
                        } else {
                            processNext(
                                context,
                                remainingImages,
                                parts,
                                callback
                            )
                        }
                    }
                }

                else -> {
                    throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
                }
            }
        }


        private fun createMultipartBodyPart(
            file: File,
            parts: MutableList<MultipartBody.Part>,
            onComplete: () -> Unit
        ) {
            Log.d("SendFilesUtil", "Creating multipart body part for file: ${file.name}, exists: ${file.exists()}, size: ${file.length()}")
            val mimeType = when (file.extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "tmp" -> "image/jpeg"
                else -> {
                    Log.e("SendFilesUtil", "Unsupported file type: ${file.extension}")
                    throw IllegalArgumentException("Unsupported file type: ${file.extension}")
                }
            }

            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
            parts.add(MultipartBody.Part.createFormData("files", file.name, requestFile))
            onComplete()
        }
    }
}