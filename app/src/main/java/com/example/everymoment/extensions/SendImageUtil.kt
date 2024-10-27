package com.example.everymoment.extensions

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SendImageUtil {

    fun sendImage(body: MultipartBody.Part){
//        apiService.sendImage(body).enqueue(object: Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if(response.isSuccessful){
//                    Log.d("sendImage", "이미지 전송 성공")
//                }else{
//                    Log.d("sendImage", "이미지 전송 실패")
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("testt", t.message.toString())
//            }
//
//        })
    }

    fun prepareFilePart(context: Context, partName: String, fileUri: Uri) {
        val file = File(absolutelyPath(fileUri, context))
        val requestFile = RequestBody.create(
            context.contentResolver.getType(fileUri)?.toMediaTypeOrNull(),
            file
        )
        val body = MultipartBody.Part.createFormData(partName, file.name, requestFile)
        sendImage(body)
    }

    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)
        return result!!
    }
}