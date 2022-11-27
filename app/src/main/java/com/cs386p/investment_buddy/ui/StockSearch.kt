package com.cs386p.investment_buddy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cs386p.investment_buddy.databinding.ActivitySearchBinding

class StockSearch : AppCompatActivity() {
    private lateinit var activitySearchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)
    }
}