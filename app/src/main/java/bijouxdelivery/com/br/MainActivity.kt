package bijouxdelivery.com.br

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)


        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetwork
        if(info == null){

            var dialogconexao = AlertDialog.Builder(this)
            dialogconexao.setTitle("Conexão")
            dialogconexao.setMessage("Você não está conectado a internet.")
            dialogconexao.setPositiveButton("SAIR") { dialog, which ->
                 this.finishAffinity()
            }
            dialogconexao.show()

        }else {


            Timer().schedule(object : TimerTask() {
                override fun run() {



                    val policy = StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
                    StrictMode.setThreadPolicy(policy)

                    val key = getSharedPreferences("key", Context.MODE_PRIVATE)
                    val codi = key.getString("key", "")!!.toString()


                    if (codi.toString() != "" || codi.toString() != "null") {
                        val client = OkHttpClient.Builder()
                                .readTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .build()

                        val request = Request.Builder()
                                .url("https://www.bijouxdelivery.com.br/home/removersacola?codcliente=" + codi)
                                .build()


                        client.newCall(request).execute()

                    }
                    val intent = Intent(this@MainActivity, homeLayout::class.java)
                    startActivity(intent)
                    finish()

                }
            }, 1000)
        }

    }
}