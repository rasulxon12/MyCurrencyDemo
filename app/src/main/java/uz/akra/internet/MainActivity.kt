package uz.akra.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.akra.internet.adapters.MyRvAdapter
import uz.akra.internet.databinding.ActivityMainBinding
import uz.akra.internet.databinding.ItemDialogBinding
import uz.akra.internet.models.MyCurrency
import uz.akra.internet.utils.MyNetworkHelper
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), MyRvAdapter.RvAction {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var myNetworkHelper: MyNetworkHelper
    lateinit var myRvAdapter: MyRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        myNetworkHelper = MyNetworkHelper(this)


        MyTask().execute()


    }

    inner class MyTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {

            var url = "https://cbu.uz/uzc/arkhiv-kursov-valyut/json/"
            var connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            val inputStream = connection.inputStream
            val bufferedReader = inputStream.bufferedReader()


            return bufferedReader.readLine()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d(TAG, "onPostExecute: $result")

            val typeToken = object : TypeToken<ArrayList<MyCurrency>>() {}.type
            val list = Gson().fromJson<ArrayList<MyCurrency>>(result, typeToken)
            myRvAdapter = MyRvAdapter(list, this@MainActivity)
            binding.myContainer.adapter = myRvAdapter


        }
    }


//

//    


    private fun isNetworkConnected(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetwork
        val networkCapabilities = manager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onClick(myCurrency: MyCurrency, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)

        itemDialogBinding.edtChetel.addTextChangedListener {
            if (it.toString() != "") {
                itemDialogBinding.edtUzbSom.setText(
                    (myCurrency.Rate.toDouble() * it.toString().toDouble()).toString()
                )
            }
        }

//        itemDialogBinding.edtUzbSom.addTextChangedListener {
//            if (it.toString() != ""){
//                itemDialogBinding.edtChetel.setText(
//                    (it.toString().toDouble() / myCurrency.Rate.toDouble()).toString()
//                )
//            }
//
//
//        }

        dialog.setView(itemDialogBinding.root)
        dialog.show()
    }


}



