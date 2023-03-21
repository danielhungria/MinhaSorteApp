package br.com.minhasortemegasena

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.minhasortemegasena.databinding.ActivityMainBinding
import br.com.minhasortemegasena.ui.main.MainFragment
import br.com.minhasortemegasena.ui.resultlist.ResultListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.navigationbarActivityMenu.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setItemActiveIndicator(destination)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setItemActiveIndicator(destination: NavDestination) {
        val allowedDestinations = listOf(
            R.id.home_list_games_caixa_fragment,
            R.id.home_list_results,
            R.id.support_fragment
        )
        binding.navigationbarActivityMenu.isItemActiveIndicatorEnabled =
            destination.id in allowedDestinations
    }

}