package br.com.minhasortemegasena

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.com.minhasortemegasena.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var mAdView: AdView

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAD()
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

    private fun setupAD() {
        mAdView = binding.adViewMain
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}