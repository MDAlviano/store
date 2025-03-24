package com.alvian.esemkastore.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alvian.esemkastore.R
import com.alvian.esemkastore.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id = intent.getIntExtra("ID", 0)
        val name = intent.getStringExtra("NAME")

        val fHome = HomeFragment()
        val bundle = Bundle()
        bundle.putInt("ID", id)
        bundle.putString("NAME", name)
        fHome.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fHome, HomeFragment::class.java.simpleName)
            .commit()

    }
}