package com.example.todoapphomework4.activityFragmentUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.todoapphomework4.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        val state = intent.getStringExtra("state")

        if (state.equals("home")) graph.startDestination = R.id.homeFragment
        else graph.startDestination = R.id.loginFragment

        navHostFragment.navController.graph = graph
    }
}