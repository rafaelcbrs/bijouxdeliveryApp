package bijouxdelivery.com.br

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.menu_drawer.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class homeLayout : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_layout)


        window.navigationBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)

        val policy = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        StrictMode.setThreadPolicy(policy)


        toastHome("Aplicativo em testes, não possível compras no momento.")


       btoVender.setOnClickListener {

           val editTextsenha = EditText(this)
           editTextsenha.setInputType(InputType.TYPE_CLASS_NUMBER)
           editTextsenha.filters = arrayOf(InputFilter.LengthFilter(11))
           editTextsenha.setBackgroundColor(Color.parseColor("#75244c"))
           editTextsenha.setTextColor(Color.WHITE)
           val dialogLogin = AlertDialog.Builder(this)
           dialogLogin.setTitle("Login")
           dialogLogin.setMessage("Digite seu CPF.")
           dialogLogin.setView(editTextsenha)
           dialogLogin.setPositiveButton("ENTRAR"){dialog, which ->
               if(editTextsenha.text.toString() != "null" && editTextsenha.text.length == 11){

                   val key = getSharedPreferences("key", Context.MODE_PRIVATE)

                   val codi = key.getString("key", "")!!.toString()

                   val client = OkHttpClient.Builder()
                           .readTimeout(10, TimeUnit.SECONDS)
                           .writeTimeout(10, TimeUnit.SECONDS)
                           .connectTimeout(10, TimeUnit.SECONDS)
                           .build()


                   val request = Request.Builder()
                           .url("https://www.bijouxdelivery.com.br/home/logarvendas?codcliente=" + codi!!.toString() + "&senha=" + editTextsenha.text.toString())
                           .build()


                   val response = client.newCall(request).execute()

                   val resposta = response.body?.string()


                   if(resposta == "true"){
                       val intent = Intent(this, vendasActivity::class.java)
                       intent.putExtra("cpf", editTextsenha.text.toString())
                       startActivity(intent)
                       finish()
                   }else{
                       toastHome("Usuário não registrado.")
                   }

               }else{
                   toastHome("Digite seu cpf.")
               }
           }

           dialogLogin.setNegativeButton("CADASTRAR"){ dialog, which ->

               if(editTextsenha.text.toString() != "null" && editTextsenha.text.length == 11) {


                   val key = getSharedPreferences("key", Context.MODE_PRIVATE)

                   val client = OkHttpClient.Builder()
                           .readTimeout(10, TimeUnit.SECONDS)
                           .writeTimeout(10, TimeUnit.SECONDS)
                           .connectTimeout(10, TimeUnit.SECONDS).build()

                   val codi = key.getString("key", "")!!.toString()

                   val formBody = FormBody.Builder()
                           .add("codcliente", codi!!.toString()).add("senha", editTextsenha.text.toString())
                           .build()
                   val request = Request.Builder()
                           .url("https://www.bijouxdelivery.com.br/home/cadastrarloginvendas")
                           .post(formBody)
                           .build()

                   val cli = client.newCall(request).execute()

                   val resposta = cli.body?.string()

                   if(resposta.toString() == "true"){
                       toastHome("Cadastro realizado.")
                   }else{
                       toastHome("Esta conta já existe.")
                   }

               }else{
                   toastHome("Digite seu cpf.")
               }

           }
           dialogLogin.show()

       }

        btoListaProdutos.setOnClickListener {
            val editTextsenha = EditText(this)
            editTextsenha.setInputType(InputType.TYPE_CLASS_NUMBER)
            editTextsenha.filters = arrayOf(InputFilter.LengthFilter(11))
            editTextsenha.setBackgroundColor(Color.parseColor("#75244c"))
            editTextsenha.setTextColor(Color.WHITE)
            val dialogLogin = AlertDialog.Builder(this)
            dialogLogin.setTitle("Login")
            dialogLogin.setMessage("Digite seu CPF.")
            dialogLogin.setView(editTextsenha)
            dialogLogin.setPositiveButton("ENTRAR"){dialog, which ->
                if(editTextsenha.text.toString() != "null" && editTextsenha.text.length == 11){

                    val key = getSharedPreferences("key", Context.MODE_PRIVATE)

                    val codi = key.getString("key", "")!!.toString()

                    val client = OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build()


                    val request = Request.Builder()
                            .url("https://www.bijouxdelivery.com.br/home/logarvendas?codcliente=" + codi!!.toString() + "&senha=" + editTextsenha.text.toString())
                            .build()


                    val response = client.newCall(request).execute()

                    val resposta = response.body?.string()


                     if(resposta == "true"){
                         val cpf = getSharedPreferences("cpf", Context.MODE_PRIVATE)
                         cpf.edit().putString("cpf", editTextsenha.text.toString()).apply()
                         var intent = Intent(this, listavendasActivity::class.java)
                         startActivity(intent)
                         finish()
                     }else{
                         toastHome("Usuário não registrado.")
                     }

                }else{
                    toastHome("Digite seu cpf.")
                }
            }

            dialogLogin.setNegativeButton("CADASTRAR"){ dialog, which ->

                if(editTextsenha.text.toString() != "null" && editTextsenha.text.length == 11) {


                    val key = getSharedPreferences("key", Context.MODE_PRIVATE)

                    val client = OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS).build()

                    val codi = key.getString("key", "")!!.toString()

                    val formBody = FormBody.Builder()
                            .add("codcliente", codi!!.toString()).add("senha", editTextsenha.text.toString())
                            .build()
                    val request = Request.Builder()
                            .url("https://www.bijouxdelivery.com.br/home/cadastrarloginvendas")
                            .post(formBody)
                            .build()

                    val cli = client.newCall(request).execute()

                    val resposta = cli.body?.string()

                    if(resposta.toString() == "true"){
                        toastHome("Cadastro realizado.")
                    }else{
                        toastHome("Esta conta já existe.")
                    }

                }else{
                    toastHome("Digite seu cpf.")
                }

            }
            dialogLogin.show()
        }





        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 1)


        val menu = findViewById<ImageButton>(R.id.btoMenuDrawer)

        menu.setOnClickListener {
            val menudrawer = findViewById<DrawerLayout>(R.id.menuDrawer)

            if(menudrawer.isDrawerOpen(GravityCompat.START)) {
                menudrawer.closeDrawer(GravityCompat.START)
            }else{
                menudrawer.openDrawer(GravityCompat.START)
            }
        }

        icocesta.setOnClickListener {
            val intent = Intent(this, SacolaActivity::class.java)
            startActivity(intent)
            finish()
        }

        icolocaliza.setOnClickListener {
            val intent = Intent(this, rastreio::class.java)
            startActivity(intent)
            finish()
        }


        navView.setNavigationItemSelectedListener { menuItem ->

            val key = getSharedPreferences("key", Context.MODE_PRIVATE)
            val d = key.getString("key", "")

            if(d == "") {
                val diagTelefone = AlertDialog.Builder(this)
                diagTelefone.setTitle("Insira seu telefone.")
                val edittext = EditText(this)
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER)
                edittext.id = 1
                diagTelefone.setView(edittext)
                diagTelefone.setPositiveButton("Entrar") { dialog, which ->
                    val key = getSharedPreferences("key", Context.MODE_PRIVATE)
                    key.edit().putString("key", edittext.text.toString()).apply()
                    val d = key.getString("key", "")
                }
                diagTelefone.show()
            }else{

                val id = menuItem.title


               if(id == "Anéis"){
                   abrirListaProdutos("1", "Anéis")
               }else if(id == "Brincos") {
                  abrirListaProdutos("2", "Brincos")
               }else if(id == "Colares") {
                   abrirListaProdutos("3", "Colares")
               }else if(id == "Pulseiras") {
                   abrirListaProdutos("4", "Pulseiras")
               }else if(id == "Tiaras") {
                   abrirListaProdutos("5", "Tiaras")
               }else if(id == "Inverno") {
                   abrirListaProdutos("6", "Inverno")
               }else if(id == "Verão") {
                   abrirListaProdutos("7", "Verão")
               }else if(id == "Máscaras") {
                   abrirListaProdutos("8", "Máscaras")
               }else if(id == "Artesanato") {
                   abrirListaProdutos("9", "Artesanato")
               }
            }

            true
        }

    }




    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_DENIED){


            val key = getSharedPreferences("key", Context.MODE_PRIVATE)
            val d = key.getString("key", "")

            if(d == "") {

                val diagTelefone = AlertDialog.Builder(this)
                diagTelefone.setTitle("Insira seu telefone.")
                val edittext = EditText(this)
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER)
                edittext.id = 1
                diagTelefone.setView(edittext)
                diagTelefone.setPositiveButton("Entrar") { dialog, which ->
                    val key = getSharedPreferences("key", Context.MODE_PRIVATE)
                    key.edit().putString("key", edittext.text.toString()).apply()
                    val d = key.getString("key", "")
                }
                diagTelefone.show()
            }
        }
        else{
            val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val key = getSharedPreferences("key", Context.MODE_PRIVATE)
            key.edit().putString("key", tm.imei).apply()
        }

    }



    fun abrirListaProdutos(valor : String, categoria : String){
        val key = getSharedPreferences("key", Context.MODE_PRIVATE)
        val d = key.getString("key", "")


        val intent = Intent(this, produtos_Activity::class.java)
        intent.putExtra("valor",  valor)
        intent.putExtra("categoria", categoria)
        startActivity(intent)
        finish()
    }


    fun toastHome(texto : String){
        val layout = layoutInflater.inflate(R.layout.custom_toast,toastlinearinfo)
        layout.txtToast.text = texto
        val myToast = Toast(applicationContext)
        myToast.duration = Toast.LENGTH_LONG
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.view = layout
        myToast.show()

    }

}