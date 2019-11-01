package net.kathir.kotlinrxjava

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.rxjava_layout.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRxJavaActivity : AppCompatActivity(),DataAdapter.Listener {

    private val TAG = RetrofitRxJavaActivity::class.java.simpleName

    private val BASE_URL = "https://learn2crack-json.herokuapp.com"

    private var mCompositeDisposal : CompositeDisposable?= null

    private var mAndroidArrayList : ArrayList<AndroidInfo>?= null

    private var mAdapter : DataAdapter?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rxjava_layout)


        progressBar.visibility = View.VISIBLE


        mCompositeDisposal = CompositeDisposable()
        initRecyclerView()

        loadJSON()




    }


    private fun initRecyclerView()
    {
        rv_android_list.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_android_list.layoutManager = layoutManager
    }

    private fun loadJSON()
    {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RequestInterface::class.java)

        mCompositeDisposal?.add(requestInterface.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse,this::handleError))
    }

    private fun handleResponse(androidInfoList:List<AndroidInfo>)
    {
        mAndroidArrayList = ArrayList(androidInfoList)
        mAdapter = DataAdapter(mAndroidArrayList!!,this)
        rv_android_list.adapter = mAdapter

        progressBar.visibility = View.GONE


    }

    private fun handleError(error: Throwable) {

        Log.d(TAG, error.localizedMessage)

        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(androidInfo:AndroidInfo)
    {
        Toast.makeText(this, "${androidInfo.name} Clicked !", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposal?.clear()
    }

}