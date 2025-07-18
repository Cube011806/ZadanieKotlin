package com.kk.zadaniekotlin

import SharedViewModel
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.FirebaseApp
import com.kk.zadaniekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        setSupportActionBar(findViewById(R.id.topBar))
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val toolbar = findViewById<Toolbar>(R.id.topBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_24px)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    sharedViewModel.setCatId(0)
                    sharedViewModel.setSubCatId(0)
                }
            }
            navController.navigate(item.itemId)
            true
        }


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //!!!!!!!!!!!!!!!
        sharedViewModel.setCatId(0)
        sharedViewModel.setSubCatId(0)
        //!!!!!!!!!!!!!!!
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.navigation_home)
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}