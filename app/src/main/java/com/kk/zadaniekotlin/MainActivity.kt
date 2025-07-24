package com.kk.zadaniekotlin

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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.kk.zadaniekotlin.databinding.ActivityMainBinding
import com.kk.zadaniekotlin.ui.basket.BasketViewModel
import java.util.Locale
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var mainViewModel: MainViewModel
    private val basketViewModel: BasketViewModel by viewModels()

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val loggedIn = result.data?.getBooleanExtra("isLoggedIn", false) ?: false
                if (loggedIn) {
                    mainViewModel.loginSuccess()
                    basketViewModel.loadCartFromFirebase()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as MyApplication).appComponent.inject(this)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView: BottomNavigationView = binding.navView

        setupActionBar(navController)

        observeViewModel(navController, navView)

        handleIntent(intent)

        navView.setupWithNavController(navController)
        setupBottomNavigation(navView, navController)

        invalidateOptionsMenu()
    }

    private fun setupActionBar(navController: androidx.navigation.NavController) {
        setSupportActionBar(findViewById(R.id.topBar))
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)

        val drawable = ContextCompat.getDrawable(this, R.drawable.arrow_back_24px)
        drawable?.setTint(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar?.setHomeAsUpIndicator(drawable)
    }

    private fun observeViewModel(navController: androidx.navigation.NavController, navView: BottomNavigationView) {
        mainViewModel.isUserLoggedIn.observe(this) { loggedIn ->
            invalidateOptionsMenu()
            if (loggedIn) basketViewModel.loadCartFromFirebase()
        }

        mainViewModel.navigateToBasket.observe(this) { navigate ->
            if (navigate) {
                navController.navigate(R.id.navigation_basket)
                mainViewModel.clearBasketNavigationFlag()
            }
        }

        basketViewModel.cartItems.observe(this) { items ->
            val badge = navView.getOrCreateBadge(R.id.navigation_basket)
            badge.number = items.size
            badge.isVisible = items.isNotEmpty()
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("openBasket", false) == true ||
            intent?.getStringExtra("navigateTo") == "basket") {
            mainViewModel.triggerBasketNavigation()
        }
    }

    private fun setupBottomNavigation(navView: BottomNavigationView, navController: androidx.navigation.NavController) {
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    navController.navigate(item.itemId)
                    true
                }
                R.id.navigation_more -> {
                    showMoreMenu(navView.findViewById(R.id.navigation_more))
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mainViewModel.isUserAuthenticated()) {
            mainViewModel.loginSuccess()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_login)?.isVisible = !(mainViewModel.isUserLoggedIn.value ?: false)
        menu?.findItem(R.id.menu_logout)?.isVisible = mainViewModel.isUserLoggedIn.value ?: false
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
            R.id.action_switch_mode -> {
                val newMode = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
                AppCompatDelegate.setDefaultNightMode(newMode)
                recreate()
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

        popup.menu.findItem(R.id.menu_logout)?.isVisible = mainViewModel.isUserAuthenticated()

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_change_language -> {
                    val languages = resources.getStringArray(R.array.language_options)
                    AlertDialog.Builder(this)
                        .setTitle("Wybierz język")
                        .setItems(languages) { _, which ->
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
                    mainViewModel.logout()
                    val badge = binding.navView.getBadge(R.id.navigation_basket)
                    badge?.isVisible = false
                    badge?.clearNumber()

                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navController.popBackStack(R.id.navigation_home, false)
                    navController.navigate(R.id.navigation_home)

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
