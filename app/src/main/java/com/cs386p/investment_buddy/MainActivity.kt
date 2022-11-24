package com.cs386p.investment_buddy

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    val api_key = "RUXI1LX1OCUM137J" // TODO: Move this, just noting this here for now
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.symbolSearch() //TODO: Delete me - just for testing
    }
}