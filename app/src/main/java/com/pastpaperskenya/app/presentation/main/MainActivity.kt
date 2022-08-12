package com.pastpaperskenya.app.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.databinding.ActivityMainBinding
import com.pastpaperskenya.app.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var navHostController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment= supportFragmentManager.findFragmentById(R.id.main_fragments_container) as NavHostFragment

        navHostController= navHostFragment.navController

        setupActionBar()
        binding.bottomNavigationView.setupWithNavController(navHostController)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbar)
        appBarConfiguration= AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.cartFragment, R.id.downloadsFragment, R.id.profileFragment)
        )

        binding.apply {
            toolbar.setupWithNavController(navHostController, appBarConfiguration)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navHostController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun onAuthStateChanged(firebaseauth: FirebaseAuth) {
        val firebaseUser= firebaseauth.currentUser
        if (firebaseUser==null){
            val intent= Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }


}