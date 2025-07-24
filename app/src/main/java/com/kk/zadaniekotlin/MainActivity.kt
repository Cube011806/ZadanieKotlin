package com.kk.zadaniekotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import com.kk.zadaniekotlin.ui.basket.BasketViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private val basketViewModel: BasketViewModel by viewModels()
    private var isUserLoggedIn: Boolean = false

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val loggedIn = result.data?.getBooleanExtra("isLoggedIn", false) ?: false
                if (loggedIn || FirebaseAuth.getInstance().currentUser != null) {
                    isUserLoggedIn = true
                    basketViewModel.loadCartFromFirebase()
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
        val channel = NotificationChannel(
            "basket_channel",
            "Koszyk",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val shouldOpenBasket = intent.getBooleanExtra("openBasket", false)
        if (shouldOpenBasket) {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_basket)
        }

        isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

        if (isUserLoggedIn) {
            basketViewModel.loadCartFromFirebase()
        }
        if (intent?.getStringExtra("navigateTo") == "basket") {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_basket)
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView: BottomNavigationView = binding.navView

        setSupportActionBar(findViewById(R.id.topBar))
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)

        val drawable = ContextCompat.getDrawable(this, R.drawable.arrow_back_24px)
        drawable?.setTint(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar?.setHomeAsUpIndicator(drawable)

        val badge = navView.getOrCreateBadge(R.id.navigation_basket)
        badge.number = basketViewModel.cartItems.value?.size ?: 0
        badge.isVisible = true

        basketViewModel.cartItems.observe(this) { items ->
            val badge = navView.getOrCreateBadge(R.id.navigation_basket)
            badge.number = items.size
            badge.isVisible = items.isNotEmpty()
        }

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

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            popup.menu.findItem(R.id.menu_logout)?.isVisible = false
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_language -> {
                    val languages = resources.getStringArray(R.array.language_options)
                    AlertDialog.Builder(this)
                        .setTitle("Wybierz język")
                        .setItems(languages) { dialog, which ->
                            val selected = languages[which]
                            when (selected) {
                                "Polski" -> setAppLocale("pl")
                                "English" -> setAppLocale("en")
                            }
                        }
                        .setNegativeButton("Anuluj", null)
                        .show()
                    true
                }

                R.id.menu_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    invalidateOptionsMenu()
                    isUserLoggedIn = false

                    val navView = binding.navView
                    val badge = navView.getBadge(R.id.navigation_basket)
                    badge?.isVisible = false
                    badge?.clearNumber()

                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navController.popBackStack(R.id.navigation_home, false)
                    navController.navigate(R.id.navigation_home)

                    Toast.makeText(this, "Wylogowano", Toast.LENGTH_SHORT).show()
                    onPrepareOptionsMenu(binding.topBar.menu)
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
    private fun setAppLocale(language: String) {
        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }

}
