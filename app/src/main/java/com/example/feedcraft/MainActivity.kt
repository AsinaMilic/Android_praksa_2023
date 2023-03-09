package com.example.feedcraft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.feedcraft.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment  //as (cast)
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)


        /*val btnOk = findViewById<Button>(R.id.btnOk)
        btnOk.setOnClickListener{
            val retIntent = Intent()
            retIntent.putExtra("main_extra","ok")
            setResult(RESULT_OK)
            finish()
        }

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }*/

    }
}