package bijouxdelivery.com.br

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bijouxdelivery.com.br.Model.DadosCep
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_vendas.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.toolbarvendas.*
import okhttp3.*
import okhttp3.OkHttpClient
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


class vendasActivity : AppCompatActivity() {

    public var categoria: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendas)

        val policy = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        StrictMode.setThreadPolicy(policy)





        window.navigationBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)

        txtTituloVendas.text = "Vender"

        editEstado.isEnabled = false
        editCidade.isEnabled = false
        barradeprogrsso.visibility = View.INVISIBLE

        editCep.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editCep.length() == 8) {
                    val client = OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS).build()


                    val request = Request.Builder()
                            .url("https://viacep.com.br/ws/" + editCep.text.toString() + "/json")
                            .build()

                    try {


                        val cli = client.newCall(request).execute()

                        val respostap = cli.body?.string()

                        val gson = Gson()

                        val publisher = gson.fromJson(respostap, DadosCep::class.java)


                        editEstado.text = publisher.ufs.toEditable()
                        editCidade.text = publisher.localidades.toEditable()
                    } catch (ex: Exception) {

                        toastInformacao("CEP não encontrado, digite um CEP válido.")


                    }
                }

            }
        })



        icovoltar.setOnClickListener {

            val intent = Intent(this, homeLayout::class.java)
            startActivity(intent)
            finish()
        }

        btoPhoto.setOnClickListener {
            openFile()
        }

        val languages = resources.getStringArray(R.array.Languages)
        val spinner = findViewById<Spinner>(R.id.selectCategoria)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, languages
            )
            spinner.adapter = adapter
        }

        btoGravarProduto.setOnClickListener {

            val cpf = intent.getStringExtra("cpf")

            val policyi = StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            StrictMode.setThreadPolicy(policyi)

            val img = findViewById<ImageView>(R.id.imgProdutoVendas)


             if (img.drawable == null) {
                toastInformacao("Selecione uma foto do produto.")
            }
           else if (editNomeProduto.text.toString() == "") {
                toastInformacao("Escreva algo sobre o produto.")
            } else if (editQuantidae.text.toString() == "") {
                toastInformacao("Coloque a quantidade do produto disponível.")
            } else if (editValor.text.toString() == "") {
                toastInformacao("Coloque o valor do produto.")
            } else if (editFornecedor.text.toString() == "") {
                toastInformacao("Coloque o nome do fornecedor.")
            } else if (editCep.text.toString() == "") {
                toastInformacao("Insira seu CEP.")
            } else if (editEstado.text.toString() == "") {
                toastInformacao("Insira seu CEP.")
            } else if (editCidade.text.toString() == "") {
                toastInformacao("Insira seu CEP.")
            } else {

                val position = selectCategoria.getSelectedItemPosition();

                if (position.toString() == "0") {
                    categoria = 1
                } else if (position.toString() == "1") {
                    categoria = 2
                } else if (position.toString() == "2") {
                    categoria = 3
                } else if (position.toString() == "3") {
                    categoria = 4
                } else if (position.toString() == "4") {
                    categoria = 5
                } else if (position.toString() == "5") {
                    categoria = 6
                } else if (position.toString() == "6") {
                    categoria = 7
                } else if (position.toString() == "7") {
                    categoria = 8
                } else if (position.toString() == "8") {
                    categoria = 9
                }


                 val key = this.getSharedPreferences("key", Context.MODE_PRIVATE)

                 val client = OkHttpClient.Builder()
                         .readTimeout(10, TimeUnit.SECONDS)
                         .writeTimeout(10, TimeUnit.SECONDS)
                         .connectTimeout(10, TimeUnit.SECONDS).build()

                 val codi = key.getString("key", "")!!.toString()


                 val formBody = FormBody.Builder()
                         .add("codcategoriap", categoria!!.toString())
                         .add("produto", editNomeProduto.text.toString())
                         .add("quantidadep", editQuantidae.text.toString())
                         .add("valorproduto", editValor.text.toString())
                         .add("codvendedorp", codi!!.toString())
                         .add("fornecedor", editFornecedor.text.toString())
                         .add("estadop", editEstado.text.toString())
                         .add("cidadep", editCidade.text.toString())
                         .add("cpf", cpf!!.toString())
                         .build()
                 val request = Request.Builder()
                         .url("https://www.bijouxdelivery.com.br/home/savarproduto")
                         .post(formBody)
                         .build()

                 val cli = client.newCall(request).execute()

                 val resposta = cli.body?.string()


                 val storage = Firebase.storage("gs://bijouxdelivery-a2350.appspot.com")

                 var storageRef = storage.reference


                 val mountainsRef = storageRef.child(resposta.toString() + ".jpg")


                 val bitmap = (imgProdutoVendas.drawable as BitmapDrawable).bitmap
                 val baos = ByteArrayOutputStream()
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                 val data = baos.toByteArray()

                 var uploadTask = mountainsRef.putBytes(data)
                 uploadTask.addOnFailureListener {
                     // Handle unsuccessful uploads
                 }.addOnSuccessListener { taskSnapshot ->

                     barradeprogrsso.visibility = View.INVISIBLE

                     toastInformacao("Cadastro do produto realizado.")

                 }

                 uploadTask.addOnProgressListener { taskSnapshot ->

                     barradeprogrsso.visibility = View.VISIBLE

                 }
            }

        }


    }

    fun toastInformacao(texto: String) {
        val layout = layoutInflater.inflate(R.layout.custom_toast, toastlinearinfo)
        layout.txtToast.text = texto
        val myToast = Toast(applicationContext)
        myToast.duration = Toast.LENGTH_LONG
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.view = layout
        myToast.show()

    }

    val IMAGEM_INTERNA = 1

    fun openFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/jpeg")
        startActivityForResult(intent, IMAGEM_INTERNA)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val patch  = Uri.parse(data?.dataString)
        imgProdutoVendas.setImageURI(patch)


    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


}

