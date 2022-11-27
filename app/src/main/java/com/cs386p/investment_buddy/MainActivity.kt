package com.cs386p.investment_buddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.cs386p.investment_buddy.databinding.ActivityMainBinding
import com.cs386p.investment_buddy.databinding.ContentMainBinding
import com.cs386p.investment_buddy.ui.StockSearch

class MainActivity : AppCompatActivity() {
    val api_key = "RUXI1LX1OCUM137J" // TODO: Move this, just noting this here for now
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding : ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        binding = activityMainBinding.contentMain

        // Set default mode for app to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Start StockSearch activity when analyze text view is clicked
        binding.analyze.setOnClickListener {
            val stockSearchClass = StockSearch()
            val intent = Intent(this, stockSearchClass::class.java)
            this.startActivity(intent)
        }
    }
}