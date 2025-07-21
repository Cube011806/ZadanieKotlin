package com.kk.zadaniekotlin

import SharedViewModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.kk.zadaniekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private var isUserLoggedIn: Boolean = false

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val loggedIn = result.data?.getBooleanExtra("isLoggedIn", false) ?: false
                if (loggedIn || FirebaseAuth.getInstance().currentUser != null) {
                    isUserLoggedIn = true
                    invalidateOptionsMenu()
                    supportActionBar?.title = "Witaj!"
                    Toast.makeText(this, "Logowanie zakończone!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView: BottomNavigationView = binding.navView

        setSupportActionBar(findViewById(R.id.topBar))
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)
        val drawable = ContextCompat.getDrawable(this, R.drawable.arrow_back_24px)
        drawable?.setTint(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar?.setHomeAsUpIndicator(drawable)

        //supportActionBar?.setHomeAsUpIndicator(drawable)

        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    sharedViewModel.setCatId(0)
                    sharedViewModel.setSubCatId(0)
                    navController.navigate(item.itemId)
                    true
                }
                R.id.navigation_more -> {
                    val itemView = navView.findViewById<View>(R.id.navigation_more)
                    showMoreMenu(itemView)
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }

        invalidateOptionsMenu()
    }

    override fun onResume() {
        super.onResume()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            isUserLoggedIn = true
            invalidateOptionsMenu()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_login)?.isVisible = !isUserLoggedIn
        menu?.findItem(R.id.menu_logout)?.isVisible = isUserLoggedIn
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                loginLauncher.launch(Intent(this, LoginActivity::class.java))
                true
            }
            android.R.id.home -> {

                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMoreMenu(anchorView: View) {
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(R.menu.more_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_language -> {
                    Toast.makeText(this, "Zmień język kliknięte", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    isUserLoggedIn = false
                    invalidateOptionsMenu()
                    supportActionBar?.title = getString(R.string.app_name)
                    Toast.makeText(this, "Wylogowano", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        sharedViewModel.setCatId(0)
        sharedViewModel.setSubCatId(0)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
