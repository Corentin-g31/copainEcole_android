package com.example.app_copains_ecole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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
    lateinit var progressBar: ProgressBar
    lateinit var tvError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bind pointeur et id
        btnRegister = findViewById(R.id.btnRegister)
        btnFind = findViewById(R.id.btnFind)
        btnLogin = findViewById(R.id.btnLogin)
        txtPseudo = findViewById(R.id.txtPseudo)
        txtPassword = findViewById(R.id.txtPassword)
        tvError = findViewById(R.id.tvError)
        progressBar = findViewById(R.id.progressBar)

        // Event listener on btn
        btnRegister.setOnClickListener(this)
        btnFind.setOnClickListener(this)
        btnLogin.setOnClickListener(this)

        // ProgressBar init a false + errorTv a gone + ""
        showProgressBar(false)
        setErrorOnUiThread("")
    }

    override fun onClick(v: View?) {
        when (v) {
            btnRegister -> {
                Log.i("tag_i", "onClick: btnRegister")
                startActivity(Intent(this, RegisterActivity::class.java))
                //val user = UserBean(pseudo = "${txtPseudo.text}", password = "${txtPassword.text}")

            }
            btnFind -> {
                Log.i("tag_i", "onClick: btnFind")
                startActivity(Intent(this, MapsActivity::class.java))
                val user = UserBean(pseudo = "${txtPseudo.text}", password = "${txtPassword.text}")
            }
            btnLogin -> {
                Log.i("tag_i", "onClick: btnLogin")
                val intent = Intent(this, MapsActivity::class.java)
                val user = UserBean(pseudo = "${txtPseudo.text}", password = "${txtPassword.text}")

                // ProgressBar le temps du login
                showProgressBar(true)

                // Lance un thread pour ne pas bloquer le thread graphique
                thread {
                    try {
                        WsUtils.login(user)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        println("Login OK : " + user.pseudo)
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

    fun setErrorOnUiThread(text: String?) = runOnUiThread {
        if (text.isNullOrBlank()) {
            tvError.visibility = View.GONE
        } else {
            tvError.visibility = View.VISIBLE
        }
        tvError.text = text
    }

    private fun showProgressBar(visible: Boolean) = runOnUiThread {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }



}


