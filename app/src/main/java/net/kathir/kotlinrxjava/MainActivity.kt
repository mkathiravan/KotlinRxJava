package net.kathir.kotlinrxjava

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kotlinRxJava.setOnClickListener {

            startActivity(Intent(this@MainActivity,RetrofitRxJavaActivity::class.java))
        }

        kotlinUploadImage.setOnClickListener {

            startActivity(Intent(this@MainActivity,RetrofitImageUploadActivity::class.java))
        }
    }
}
