package com.alvian.esemkastore.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alvian.esemkastore.R
import com.alvian.esemkastore.repository.AuthRepository
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.inputEmail).text.toString()
            val pass = findViewById<TextInputEditText>(R.id.inputPass).text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                doLogin(email, pass)
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun doLogin(email: String, pass: String) {
        thread {
            val response = AuthRepository.login(email, pass)
            try {
                if (response.isNotEmpty()) {
                    val jsonObject = JSONObject(response)
                    val id = jsonObject.getInt("id")
                    val name = jsonObject.getString("name")

                    if (id > 0 && name.isNotEmpty()) {
                        Intent(this, MainActivity::class.java).also {
                            it.putExtra("ID", id)
                            it.putExtra("NAME", name)
                            startActivity(it)
                        }
                    }

                } else {
                    Toast.makeText(this, "Username or password wrong!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}