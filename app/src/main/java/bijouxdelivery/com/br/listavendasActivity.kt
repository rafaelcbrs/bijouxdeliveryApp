package bijouxdelivery.com.br

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.Dados
import bijouxdelivery.com.br.Model.DadosCep
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_listavendas.*
import kotlinx.android.synthetic.main.activity_produtos_.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.toolbarlistavendas.*
import kotlinx.android.synthetic.main.toolbarprodutos.*
import kotlinx.android.synthetic.main.toolbarvendas.*
import kotlinx.android.synthetic.main.toolbarvendas.txtTituloVendas
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class listavendasActivity : AppCompatActivity() {

    private val dado = ArrayList<Dados>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listavendas)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)


        val policy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(policy)


        txtTituloVendas.text = "Vendendo"

        swipeprodutosListaVendas.setColorSchemeColors(Color.RED, Color.RED, Color.RED)
        swipeprodutosListaVendas.setOnRefreshListener {

            buscacep()
            swipeprodutosListaVendas.isRefreshing = false

        }

        icovoltarListaVendas.setOnClickListener {
            val intent = Intent(this, homeLayout::class.java)
            startActivity(intent)
            finish()
        }


        editTextPesquisaListaVendas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(editTextPesquisaListaVendas.length() == 8)
                {
                    buscacep()
                }

            }
        })

    }


     fun  buscacep(){

        val cpf = getSharedPreferences("cpf", Context.MODE_PRIVATE)
        val cpfnumber = cpf.getString("cpf", "")

        val key = getSharedPreferences("key", Context.MODE_PRIVATE)
        val d = key.getString("key", "")


        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("https://viacep.com.br/ws/"+editTextPesquisaListaVendas.text.toString()+"/json")
            .build()

        try {


            val cli = client.newCall(request).execute()

            val respostaps = cli.body?.string()

            val gsons = Gson()

            val publishers = gsons.fromJson(respostaps, DadosCep::class.java)


            val listView = findViewById<RecyclerView>(R.id.listaprodutosVendas)
            dado.clear()
            listView.recycledViewPool.clear()


            listView.setLayoutManager(GridLayoutManager(this, 1))

            val client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
            val request = Request.Builder()
                .url("https://www.bijouxdelivery.com.br/home/buscarlistavendas?codcliente=" + d.toString() + "&cpf=" + cpfnumber.toString() + "&estado=" + publishers.ufs + "&cidade=" + publishers.localidades)
                .build()


            val response = client.newCall(request).execute()
            val json = response.body?.string()

            val gson = Gson()

            val publisher = gson.fromJson(json, Array<Dados>::class.java)

            val s = publisher.count()
            val total = s - 1

            for (i in 0..total){ dado.add(
                Dados(
                    publisher.get(i).ids,
                    publisher.get(i).categoria,
                    publisher.get(i).produtos,
                    publisher.get(i).quantidades,
                    publisher.get(i).valor,
                    publisher.get(i).fornecedores,
                    publisher.get(i).codestrela,
                    publisher.get(i).estado,
                    publisher.get(i).cidade,
                    publisher.get(i).cpfvendedor

                )
            )
                listView.adapter = AdapterListaVendas(dado, this)
            }

        }catch (ex:Exception) {
            toastProdutos("CEP não encontrado, digite um CEP válido.")

        }

    }

    fun toastProdutos(texto : String){
        val layout = layoutInflater.inflate(R.layout.custom_toast,toastlinearinfo)
        layout.txtToast.text = texto
        val myToast = Toast(applicationContext)
        myToast.duration = Toast.LENGTH_LONG
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.view = layout
        myToast.show()

    }
}