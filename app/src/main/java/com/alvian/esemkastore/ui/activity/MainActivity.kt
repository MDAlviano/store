package com.alvian.esemkastore.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.alvian.esemkastore.R
import com.alvian.esemkastore.ui.fragment.CartFragment
import com.alvian.esemkastore.ui.fragment.HistoryFragment
import com.alvian.esemkastore.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var tvCartCount: TextView

    private val cartItemCount = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvCartCount = findViewById(R.id.tvCartCount)

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

        observeCartChanges()

        cartItemCount.value = 0

        setupNav()

    }

    private fun setupNav() {
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            val fHome = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fHome, HomeFragment::class.java.simpleName)
                .commit()

            findViewById<LinearLayout>(R.id.navHome).background = ResourcesCompat.getDrawable(resources, R.color.color1, null)
            findViewById<LinearLayout>(R.id.navCart).background = ResourcesCompat.getDrawable(resources, R.color.white, null)
            findViewById<LinearLayout>(R.id.navHistory).background = ResourcesCompat.getDrawable(resources, R.color.white, null)

            findViewById<TextView>(R.id.textHome).setTextColor(resources.getColor(R.color.white))
            findViewById<TextView>(R.id.tvCartCount).setTextColor(resources.getColor(R.color.black))
            findViewById<TextView>(R.id.textHistory).setTextColor(resources.getColor(R.color.black))
        }

        findViewById<LinearLayout>(R.id.navCart).setOnClickListener {
            val fCart = CartFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fCart, CartFragment::class.java.simpleName)
                .commit()

            findViewById<LinearLayout>(R.id.navHome).background = ResourcesCompat.getDrawable(resources, R.color.white, null)
            findViewById<LinearLayout>(R.id.navCart).background = ResourcesCompat.getDrawable(resources, R.color.color1, null)
            findViewById<LinearLayout>(R.id.navHistory).background = ResourcesCompat.getDrawable(resources, R.color.white, null)

            findViewById<TextView>(R.id.textHome).setTextColor(resources.getColor(R.color.black))
            findViewById<TextView>(R.id.tvCartCount).setTextColor(resources.getColor(R.color.white))
            findViewById<TextView>(R.id.textHistory).setTextColor(resources.getColor(R.color.black))
        }

        findViewById<LinearLayout>(R.id.navHistory).setOnClickListener {
            val fHistory = HistoryFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fHistory, HistoryFragment::class.java.simpleName)
                .commit()

            findViewById<LinearLayout>(R.id.navHome).background = ResourcesCompat.getDrawable(resources, R.color.white, null)
            findViewById<LinearLayout>(R.id.navCart).background = ResourcesCompat.getDrawable(resources, R.color.white, null)
            findViewById<LinearLayout>(R.id.navHistory).background = ResourcesCompat.getDrawable(resources, R.color.color1, null)

            findViewById<TextView>(R.id.textHome).setTextColor(resources.getColor(R.color.black))
            findViewById<TextView>(R.id.tvCartCount).setTextColor(resources.getColor(R.color.black))
            findViewById<TextView>(R.id.textHistory).setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun observeCartChanges() {
        cartItemCount.observe(this) { count ->
            tvCartCount.text = "CART($count)"
        }
    }

    fun updateCartCount(count: Int) {
        cartItemCount.value = count
    }

}