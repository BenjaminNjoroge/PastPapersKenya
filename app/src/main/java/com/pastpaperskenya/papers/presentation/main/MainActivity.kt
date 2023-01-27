package com.pastpaperskenya.papers.presentation.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.util.StoreTimeHelper
import com.pastpaperskenya.papers.databinding.ActivityMainBinding
import com.pastpaperskenya.papers.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private var isPressedOnce: Boolean = false

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

        if (!StoreTimeHelper.isStoreOpen()) {
            StoreTimeHelper.showCloseDialogue(this)
        }
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


    override fun onBackPressed() {
        if(isPressedOnce){
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
        isPressedOnce= true

        Handler(Looper.getMainLooper()).postDelayed({
            isPressedOnce= false
        }, 2000)
    }
}