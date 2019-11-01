package net.kathir.kotlinrxjava

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.upload_image_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class RetrofitImageUploadActivity : AppCompatActivity()
{


    private val TAG = RetrofitImageUploadActivity::class.java.simpleName
    private val INTENT_REQUEST_CODE = 100
    private var mImageUrl = ""
    val URL = "http://10.0.2.2:8080"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_image_layout)

        initViews()
    }

    private fun initViews() {

        btn_select_image.setOnClickListener {

            btn_show_image.visibility = GONE

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"

            try {
                startActivityForResult(intent, INTENT_REQUEST_CODE)

            } catch (e: ActivityNotFoundException) {

                e.printStackTrace()
            }

        }

        btn_show_image.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(mImageUrl)
            startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                try {

                    val `is` = contentResolver.openInputStream(data!!.data!!)

                    uploadImage(getBytes(`is`))

                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }
    }



    private fun getBytes(stream: InputStream?): ByteArray
    {
        val byteBuff = ByteArrayOutputStream()
        val buffSize = 1024
        val buff = ByteArray(buffSize)

        try {

           while (true) {

             val length = stream!!.read(buff)

                if (length <= 0)

                  break

               byteBuff.write(buff, 0, length)

            }
        }
        catch (t: Throwable ) {


}


    return byteBuff.toByteArray();

}





//    private fun getBytes(inputStream: InputStream?): ByteArray {
//
//        val byteBuff = ByteArrayOutputStream()
//
//        val buffSize = 1024
//        val buff = ByteArray(buffSize)
//
//        var len = 0
//        while ((len = inputStream.read(buff)) != -1) {
//            byteBuff.write(buff, 0, len)
//        }
//
//        return byteBuff.toByteArray()
//
//    }

    private fun uploadImage(imageBytes: ByteArray) {

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface = retrofit.create(RequestInterface::class.java!!)

        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes)

        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
        val call = retrofitInterface.uploadImage(body)
        progress.setVisibility(View.VISIBLE)
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                progress.visibility = View.GONE

                if (response.isSuccessful()) {

                    val responseBody = response.body()
                    btn_show_image.visibility = View.VISIBLE
                    mImageUrl = URL + responseBody!!.path
                    Snackbar.make(findViewById(R.id.content), responseBody!!.message, Snackbar.LENGTH_SHORT).show()

                } else {
                    val errorBody = response.errorBody()
                    val gson = Gson()

                    try {

                        val errorResponse =
                            gson.fromJson<Response>(errorBody!!.string(), Response::class.java!!)
                        Snackbar.make(findViewById(R.id.content), errorResponse.message, Snackbar.LENGTH_SHORT).show()

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {

                progress.visibility = View.GONE
                Log.d(TAG, "onFailure: " + t.localizedMessage)
            }
        })
    }
}