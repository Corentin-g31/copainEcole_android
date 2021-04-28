package com.example.app_copains_ecole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.app_copains_ecole.model.UserBean
import com.example.app_copains_ecole.utils.WsUtils
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Déclaration d'un pointeur vers un Button / txt ...
    lateinit var btnRegister: Button
    lateinit var btnFind: Button
    lateinit var btnLogin: Button
    lateinit var txtPseudo: EditText
    lateinit var txtPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bind pointeur et id
        btnRegister = findViewById(R.id.btnRegister)
        btnFind = findViewById(R.id.btnFind)
        btnLogin = findViewById(R.id.btnLogin)
        txtPseudo = findViewById(R.id.txtPseudo)
        txtPassword = findViewById(R.id.txtPassword)

        // Event listener on btn
        btnRegister.setOnClickListener(this)
        btnFind.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnRegister -> {
                Log.i("tag_i", "onClick: btnRegister")
//                startActivity(Intent(this, RegisterActivity::class.java))
            }
            btnFind -> {
                Log.i("tag_i", "onClick: btnFind")
//                startActivity(Intent(this, MapsActivity::class.java))
            }
            btnLogin -> {
                Log.i("tag_i", "onClick: btnLogin")
//                val intent = Intent(this, MapsActivity::class.java)
                val user = UserBean(pseudo = "${txtPseudo.text}", password = "${txtPassword.text}")

                // ProgressBar le temps du login
                showLoadingScreen(true)

                // Lance un thread pour ne pas bloquer le thread graphique
                thread {
                    try {
                        val loginUser = WsUtils.login(user)
//                        intent.putExtra("user_id", "$loginUser")
//                        startActivity(intent)
                        println(loginUser)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.w("tag_w", "${e.message}")
                        setErrorOnUiThread(e.message)
                    }
                }

            }
        }
    }


    /* -------------------------------- */
    // Méthode de mise à jour de l'ihm
    /* -------------------------------- */

    private fun setErrorOnUiThread(text: String?) = runOnUiThread {
        if (text.isNullOrBlank()) {
//            tvLoading.text = resources.getString(R.string.tvLoadingTxt)
//            tvLoading.setBackgroundColor(Color.WHITE)
//            tvLoading.setTextColor(Color.BLACK)
//            showLoadingScreen(true)
        } else {
//            weatherDatas.visibility = View.GONE
//            showLoadingScreen(true)
//            progressBar.visibility = View.INVISIBLE
//            tvLoading.setBackgroundColor(Color.RED)
//            tvLoading.setTextColor(Color.WHITE)
//            tvLoading.text = text
        }
    }

    private fun showLoadingScreen(visible: Boolean) = runOnUiThread {
//        loadingScreen.visibility = if (visible) View.VISIBLE else View.GONE
    }


}